package com.example.quizapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.model.ResponseCategoryJson
import com.example.quizapp.recycleradapters.OptionsAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_quiz.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var quizObject: List<ResponseCategoryJson>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            quizObject = it.getParcelableArrayList(QUIZ_KEY)
        }
    }


    lateinit var QuizModel: QuizViewModel

    fun getUserName(): String? {

        val userName =
            activity?.getPreferences(Context.MODE_PRIVATE)
                ?.getString(resources.getString(R.string.user_name), "Anonymous");

        return userName

    }

    val TAG = QuizFragment::class.java.simpleName
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false);



        QuizModel = activity!!.let { ViewModelProvider(it).get(QuizViewModel::class.java) }
        QuizModel.onNextItemClicked()

//        val linearLayoutManager = GridLayoutManager(context, 2);
        val linearLayoutManager = LinearLayoutManager(context);

        view.apply {


            user_name.text = getUserName()


            QuizModel.questionNumber.observe(activity!!, Observer {
                question_no.text = "Question No $it."
            })


//            a

            val optionsAdapter = OptionsAdapter()
            var correctAnswer: String = ""
            var selectedAnswer: String = ""
            var correctCount: Int = 0

            choice_recycler_view.adapter = optionsAdapter
            choice_recycler_view.layoutManager = linearLayoutManager
            optionsAdapter.onItemClick = {
                selectedAnswer = it
                Log.d(TAG, "String is $it");
            }

            next_question_btn.setOnClickListener {

                if (correctAnswer == selectedAnswer)
                    ++correctCount
                else
                    Toast.makeText(context, "correct answer is $correctAnswer", Toast.LENGTH_SHORT)
                        .show()
                QuizModel.onNextItemClicked()
            }

            QuizModel.question.observe(activity!!, Observer {

                val question = it;
                correctAnswer = question.correct_answer
                question_text.text = question.question
                optionsAdapter.setChoicelist(question.incorrect_answers)

            })


            QuizModel.completedQuiz.observe(activity!!, Observer {
                if (it) {
                    findNavController().navigate(R.id.action_quizFragment_to_finalResult);
                    activity?.viewModelStore?.clear()
                }
            })
        }

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuizFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuizFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
