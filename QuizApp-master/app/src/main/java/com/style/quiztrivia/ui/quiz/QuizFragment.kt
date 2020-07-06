package com.style.quiztrivia.ui.quiz

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.style.quiztrivia.MainActivity
import com.style.quiztrivia.getViewModelFactory
import com.style.quiztrivia.recycleradapters.OptionsAdapter
import com.style.quiztrivia.util.EventObserver
import com.google.android.material.snackbar.Snackbar
import com.style.quiztrivia.R
import com.style.quiztrivia.databinding.FragmentQuizBinding
import com.style.quiztrivia.disableViewDuringAnimation


class QuizFragment : Fragment() {


    private val args: QuizFragmentArgs by navArgs()

    private val quizViewModel by viewModels<QuizViewModel> { getViewModelFactory() }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        return inflater.inflate(R.menu.pay, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.donate -> findNavController().navigate(R.id.action_quizFragment_to_donationFragment)
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

            quizViewModel.setQuizList(args.resultQuiz)

            viewmodel = quizViewModel

            quizViewModel.userName.observe(viewLifecycleOwner, Observer {
                userName.text = it.userName
            })


            backButton.setOnClickListener {
                findNavController().navigateUp()
            }

            nextQuestionBtn.setOnClickListener {
                quizViewModel.checkAnswer(selectedAnswer)
            }

            quizViewModel.correctScoreEvent.observe(viewLifecycleOwner, EventObserver {
                colorizerGreen()
            })


            quizViewModel.correctAnwer.observe(viewLifecycleOwner, EventObserver {
                colorizeRed()
                Snackbar.make(view!!, "$it is the correct answer", Snackbar.LENGTH_SHORT).show()
            })

            quizViewModel.noOptionsSelected.observe(viewLifecycleOwner, EventObserver {
                activity!!.runOnUiThread {
                    optionsAdapter.setNoItemSelected()
                }
            })

            quizViewModel.finalScoreEvent.observe(viewLifecycleOwner,
                EventObserver {
                    val action = QuizFragmentDirections.actionQuizFragmentToFinalResult(it)
                    findNavController().navigate(action)
                })


            quizViewModel.rotateEvent.observe(viewLifecycleOwner, EventObserver {
                rotater()
            })

        }

        // Inflate the layout for this fragment
        return dataBinding.root
    }

    private fun colorizeRed() {


        var animator = ObjectAnimator.ofArgb(
            dataBinding.questionHolder,
            "backgroundColor", Color.WHITE, Color.RED
        )
        animator.setDuration(1000)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(dataBinding.questionHolder)
        animator.start()
    }

    fun colorizerGreen() {

        // Animate the color of the star's container from black to red over a half
        // second, then reverse back to black. Note that using a propertyName of
        // "backgroundColor" will cause the animator to call the backgroundColor property
        // (in Kotlin) or setBackgroundColor(int) (in Java).

        var animator = ObjectAnimator.ofArgb(
            dataBinding.questionHolder,
            "backgroundColor", Color.WHITE, Color.GREEN
        )
        animator.setDuration(1000)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(dataBinding.questionHolder)
        animator.start()
    }

    fun rotater() {

        // Rotate the view for a second around its center once
//        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, -8f)
//        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, -8f)
        val rotation = PropertyValuesHolder.ofFloat(View.ROTATION, -360f, 0f)

        val animator = ObjectAnimator.ofPropertyValuesHolder(
            dataBinding.questionHolder,
            rotation
        )

        animator.duration = 500
        animator.repeatCount = 0
        animator.disableViewDuringAnimation(dataBinding.questionHolder)
        animator.start()
    }

    override fun onPause() {
        super.onPause()
    }


    fun immersiveMode() {
        Handler().post {
            activity?.window?.decorView?.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()
        setRecyclerView()
        immersiveMode()

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
        val linearLayoutManager = LinearLayoutManager(context);
        optionsAdapter = OptionsAdapter()
        dataBinding.choiceRecyclerView.adapter = optionsAdapter
        dataBinding.choiceRecyclerView.layoutManager = linearLayoutManager

        optionsAdapter.onItemClick = {
            selectedAnswer = it
        }

    }


}
