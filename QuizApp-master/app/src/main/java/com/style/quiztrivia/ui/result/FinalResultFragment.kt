package com.style.quiztrivia.ui.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.style.quiztrivia.getViewModelFactory
import com.style.quiztrivia.R
import com.style.quiztrivia.util.EventObserver
import kotlinx.android.synthetic.main.fragment_final_result.view.*


class FinalResult : Fragment() {


    private val args: FinalResultArgs by navArgs()


    val finalViewModel by viewModels<ResultViewModel> { getViewModelFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val ratingScreenCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.rating_dialog)
            }
        }


        requireActivity().onBackPressedDispatcher.addCallback(this, ratingScreenCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_final_result, container, false)
        view.apply {


            finalViewModel.startLogoutEvent.observe(viewLifecycleOwner, EventObserver {

                Toast.makeText(context, "Log out", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_chooseCategory_to_signUpAndLogIn)

            })


            finalViewModel.startDonationEvent.observe(viewLifecycleOwner, EventObserver {

                findNavController().navigate(R.id.action_finalResult_to_donationFragment)

            })

            submit.setOnClickListener {

                val name = your_name.editText?.text.toString()

                val desc = feedback.editText?.text.toString()

                if (name.isEmpty() || desc.isEmpty()) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.name_desc_not_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }


                finalViewModel.submitFeedback(name, desc)
                findNavController().navigate(R.id.rating_dialog)


            }

            total_score.text =
                context.getString(R.string.your_total_score_is_10_10, args.finalScore.toInt())

            donate.setOnClickListener {

                finalViewModel.dontate()

            }

            again.setOnClickListener {
                findNavController().navigate(R.id.action_finalResult_to_chooseCategory)
            }

        }
        // Inflate the layout for this fragment
        return view;
    }


}
