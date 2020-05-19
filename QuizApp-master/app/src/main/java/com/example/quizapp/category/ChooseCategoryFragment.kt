package com.example.quizapp.category

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizapp.MainActivity
import com.example.quizapp.R
import com.example.quizapp.database.AppDatabase
import com.example.quizapp.database.ScoreModel
import com.example.quizapp.quiz.QuizViewModel
import com.google.firebase.auth.FirebaseAuth
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_choose_category.view.*
import kotlinx.android.synthetic.main.layout_list.view.*

class ChooseCategoryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    lateinit var chooseCategoryViewModel: QuizViewModel
    lateinit var progressDoalog: ContentLoadingProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_choose_category, container, false)

        chooseCategoryViewModel = ViewModelProvider(activity!!).get(QuizViewModel::class.java)
        view.apply {


            collapasing_toolbar.title = "Categories"
            collapasing_toolbar.setExpandedTitleColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorSurface
                )
            )



            if (activity is MainActivity) {

                (activity as MainActivity).setSupportActionBar(tool_bar)

            }



            sport_btn.setOnClickListener {
                chooseCategoryViewModel.onCategoryClicked(it.id)
            }

            myth_btn.setOnClickListener {
                chooseCategoryViewModel.onCategoryClicked(it.id)
            }

            entertainment_btn.setOnClickListener {
                chooseCategoryViewModel.onCategoryClicked(it.id)
            }



            chooseCategoryViewModel.quizRepository.errorModel.observe(activity!!, Observer {

//               todo create an alert box to show to users
                Toast.makeText(
                    context,
                    " error code:  ${it.errorCode} message ${it.errorMessaageId} ",
                    Toast.LENGTH_LONG
                ).show()


            })


            chooseCategoryViewModel.categoryList.observe(activity!!, Observer {
                it.also {
                    if (it.isNotEmpty()) {

                        content_progress_bar.visibility = View.GONE
                        chooseCategoryViewModel.setQuizObject(it)
                        findNavController().navigate(R.id.action_chooseCategory_to_quizFragment)

                    }
                }
            })

            chooseCategoryViewModel.quizRepository.isLoading.observe(activity!!, Observer {
                content_progress_bar.visibility = View.VISIBLE
            })

        }

        setHasOptionsMenu(true)


        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.delete_all_score -> {

                chooseCategoryViewModel.nukeTable()

            }
            R.id.log_out -> {


                Toast.makeText(context, "Log out", Toast.LENGTH_SHORT).show()
                FirebaseAuth.getInstance().signOut()
                clearLoginDetails()
                findNavController().navigate(R.id.action_chooseCategory_to_signUpAndLogIn)

            }
        }

        return super.onOptionsItemSelected(item)
    }


    fun clearLoginDetails() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.user_name), null)
            commit()
        }
    }


}
