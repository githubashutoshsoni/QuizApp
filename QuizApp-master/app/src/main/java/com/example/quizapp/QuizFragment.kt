package com.example.quizapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.Px
import androidx.core.view.forEach
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import com.example.quizapp.model.ResponseCategoryJson
import com.example.quizapp.model.ScoreModel
import com.example.quizapp.recycleradapters.OptionsAdapter
import io.realm.Realm
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.fragment_quiz.view.*

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

                    Realm.init(context)
                    val realm = Realm.getDefaultInstance()
                    realm.beginTransaction()


                    val params = realm.createObject<ScoreModel>();
                    params.score = correctCount
                    realm.insertOrUpdate(params)
                    realm.commitTransaction()
                    realm.close()






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

        view.choice_recycler_view.addOnScrollListener(
            OscillatingScrollListener(

                resources.getDimensionPixelSize(R.dimen.grid_2)

            )
        )

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

private const val MAX_OSCILLATION_ANGLE = 6f // ±6º

class OscillatingScrollListener(
    @Px private val scrollDistance: Int
) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        // Calculate a rotation to set from the horizontal scroll
        val clampedDx = dx.coerceIn(-scrollDistance, scrollDistance)
        val rotation = (clampedDx / scrollDistance) * MAX_OSCILLATION_ANGLE
        recyclerView.forEach {
            // Alter the pivot point based on scroll direction to make motion look more natural
            it.pivotX = it.width / 2f + clampedDx / 3f
            it.pivotY = it.height / 3f
            it.spring(SpringAnimation.ROTATION).animateToFinalPosition(rotation)
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState != SCROLL_STATE_DRAGGING) {
            recyclerView.forEach {
                it.spring(SpringAnimation.ROTATION).animateToFinalPosition(0f)
            }
        }
    }
}
