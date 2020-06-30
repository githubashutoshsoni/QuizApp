package com.example.quizapp.ui.quiz

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.MainActivity
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuizBinding
import com.example.quizapp.getViewModelFactory
import com.example.quizapp.recycleradapters.OptionsAdapter
import com.example.quizapp.util.EventObserver


class QuizFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }


    private val args: QuizFragmentArgs by navArgs()

    private val quizViewModel by viewModels<QuizViewModel> { getViewModelFactory() }

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


    var selectedAnswer: String = ""

    lateinit var optionsAdapter: OptionsAdapter

    private lateinit var dataBinding: FragmentQuizBinding


    fun setUpToolBar() {
        if (activity is MainActivity) {


            (activity as MainActivity).setSupportActionBar(dataBinding.toolBar)
            (activity as MainActivity).title = "Quiz Test"


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        setHasOptionsMenu(true)
        context?.theme?.applyStyle(R.style.QuizApp_Purple, true)

        dataBinding = FragmentQuizBinding.inflate(inflater, container, false).apply {
            viewmodel = quizViewModel

            userName.text = getUserName()


            quizViewModel.setQuizList(args.resultQuiz)

            nextQuestionBtn.setOnClickListener {
                quizViewModel.checkAnswer(selectedAnswer)
            }


            quizViewModel.finalScoreEvent.observe(viewLifecycleOwner,
                EventObserver {

                    val action = R.id.action_quizFragment_to_finalResult
                    findNavController().navigate(action)

                })


        }

        // Inflate the layout for this fragment
        return dataBinding.root
    }


    override fun onPause() {
        super.onPause()
        findNavController().popBackStack(R.id.chooseCategory, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()
        setRecyclerView()
        quizViewModel.listOptions.observe(viewLifecycleOwner, Observer {

            val list = it
            list?.let { value ->
                run {
                    optionsAdapter.setChoicelist(value)
                }
            }

        })

        dataBinding.lifecycleOwner = this.viewLifecycleOwner

    }

    fun setRecyclerView() {
//        val linearLayoutManager = GridLayoutManager(context, 2);


        val linearLayoutManager = LinearLayoutManager(context);
        optionsAdapter = OptionsAdapter()
        dataBinding.choiceRecyclerView.adapter = optionsAdapter
        dataBinding.choiceRecyclerView.layoutManager = linearLayoutManager

        optionsAdapter.onItemClick = {
            selectedAnswer = it
        }

    }


}
