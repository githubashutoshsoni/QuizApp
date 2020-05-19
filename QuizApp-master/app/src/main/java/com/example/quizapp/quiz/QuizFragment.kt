package com.example.quizapp.quiz

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.MainActivity
import com.example.quizapp.R
import com.example.quizapp.model.ResponseCategoryJson
import com.example.quizapp.database.ScoreModel
import com.example.quizapp.recycleradapters.OptionsAdapter
import io.realm.Realm
import io.realm.kotlin.createObject
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        return inflater.inflate(R.menu.pay, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.donate -> Toast.makeText(context, "Thanks for paying", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }


    val TAG = QuizFragment::class.java.simpleName

    var correctAnswer: String = ""
    var selectedAnswer: String = ""
    var correctCount: Int = 0
    var questionNumber: Int = 0;

    lateinit var optionsAdapter: OptionsAdapter
    lateinit var quizItems: ArrayList<ResponseCategoryJson>

    val QUESTION_NO = "question_no"
    val CORRECT_COUNT = "correct_count"
    val QUESTION_LIST = "question_list"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        setHasOptionsMenu(true)
        context?.theme?.applyStyle(R.style.QuizApp_Purple, true)
        val view = layoutInflater.inflate(R.layout.fragment_quiz, container, false);


        QuizModel = activity!!.let { ViewModelProvider(it).get(QuizViewModel::class.java) }

        if (savedInstanceState != null) {

            val list: ArrayList<ResponseCategoryJson> =
                savedInstanceState.getParcelableArrayList<ResponseCategoryJson>(
                    QUESTION_LIST
                ) as ArrayList<ResponseCategoryJson>

            QuizModel.setQuizObject(list)
            QuizModel.setValueOfPosition(savedInstanceState.getInt(QUESTION_NO))
            correctCount = savedInstanceState.getInt(CORRECT_COUNT)
            Log.d(
                TAG, "correct count $correctCount " +
                        "savedPosition is ${savedInstanceState.getInt(QUESTION_NO)}" +
                        "list is $list"
            )
        }

        view.apply {


            user_name.text = getUserName()
            setRecyclerView(this)


            if (activity is MainActivity) {

                val toolbar = tool_bar
                (activity as MainActivity).setSupportActionBar(toolbar)
                (activity as MainActivity).title = "Quiz Test"
                back_button.setOnClickListener {


                }


            }


        }

        // Inflate the layout for this fragment
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {

            if (savedInstanceState == null)
                QuizModel.onNextItemClicked()


            QuizModel.questionNumber.observe(activity!!, Observer {
                questionNumber = it
                question_no.text = "Question No ${questionNumber + 1}."
            })

            next_question_btn.setOnClickListener {

                if (correctAnswer == selectedAnswer)
                    ++correctCount
                else
                    Toast.makeText(
                        context,
                        "correct answer is $correctAnswer",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                Log.d(TAG, "correct answer $correctAnswer correct count $correctCount")

                QuizModel.onNextItemClicked()
            }

            QuizModel.categoryList.observe(activity!!, Observer {
                quizItems = it;
            })

            QuizModel.question.observe(activity!!, Observer {

                val question = it;
                correctAnswer = question.correct_answer
                question_text.text = question.question
                optionsAdapter.setChoicelist(question.incorrect_answers)

            })


            QuizModel.completedQuiz.observe(activity!!, Observer {
                if (it) {


//                  insert into the database completely..
                    QuizModel.insertScore(correctCount)


                    findNavController().navigate(R.id.action_quizFragment_to_finalResult);
                    activity?.viewModelStore?.clear()

                }
            })
        }
    }

    fun setRecyclerView(view: View) {
//        val linearLayoutManager = GridLayoutManager(context, 2);
        val linearLayoutManager = LinearLayoutManager(context);
        optionsAdapter = OptionsAdapter()
        view.choice_recycler_view.adapter = optionsAdapter
        view.choice_recycler_view.layoutManager = linearLayoutManager
        optionsAdapter.onItemClick = {
            selectedAnswer = it
            Log.d(TAG, "String is $it");
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList(QUESTION_LIST, quizItems)
        outState.putInt(CORRECT_COUNT, correctCount)
        outState.putInt(QUESTION_NO, questionNumber)

    }

    companion object {

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
