package com.example.quizapp.ui.ui.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.quizapp.R
import com.example.quizapp.getViewModelFactory
import com.example.quizapp.ui.result.ResultViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_final_result.view.*


class FinalResult : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    val finalViewModel by viewModels<ResultViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_final_result, container, false)
        view.apply {

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
            }


            log_out.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                findNavController().navigate(R.id.action_finalResult_to_signUpAndLogIn)
            }

            again.setOnClickListener {
                findNavController().navigate(R.id.action_finalResult_to_chooseCategory)
            }

        }
        // Inflate the layout for this fragment
        return view;
    }


}
