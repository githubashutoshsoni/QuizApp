package com.example.quizapp.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.quizapp.R
import com.example.quizapp.database.AppDatabase
import com.example.quizapp.database.ScoreModel
import com.google.firebase.auth.FirebaseAuth
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_final_result.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FinalResult.newInstance] factory method to
 * create an instance of this fragment.
 */
class FinalResult : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_final_result, container, false)
        view.apply {


            AppDatabase.getDatabase(context).scoreDao().getAll().observe(activity!!, Observer {

                //get the last inserted element from the database
                total_score.text = "You scored a total of ${it[it.size-1].score} /10"

            })


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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FinalResult.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FinalResult().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
