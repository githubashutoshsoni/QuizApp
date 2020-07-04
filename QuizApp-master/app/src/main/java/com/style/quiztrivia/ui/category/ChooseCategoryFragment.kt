package com.style.quiztrivia.ui.category

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import androidx.navigation.fragment.findNavController
import com.style.quiztrivia.MainActivity

import com.style.quiztrivia.database.Result
import com.style.quiztrivia.getViewModelFactory
import com.style.quiztrivia.util.EventObserver
import com.google.firebase.auth.FirebaseAuth
import com.style.quiztrivia.R
import com.style.quiztrivia.databinding.FragmentChooseCategoryBinding

import timber.log.Timber

class ChooseCategoryFragment : Fragment() {

    private val categoryViewModel by viewModels<CategoryViewModel> { getViewModelFactory() }


    private lateinit var viewDataBinding: FragmentChooseCategoryBinding

    lateinit var progressDoalog: ContentLoadingProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = FragmentChooseCategoryBinding.inflate(inflater, container, false).apply {
            viewmodel = categoryViewModel

            collapasingToolbar.title = "Categories"

            collapasingToolbar.setExpandedTitleColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.colorSurface
                )
            )

            if (activity is MainActivity) {

                (activity as MainActivity).setSupportActionBar(toolBar)

            }


        }


        categoryViewModel.quizList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Loading -> Timber.d("Loading")
                is Result.Success -> Timber.d(" got the result")
                is Result.Error -> Toast.makeText(
                    context,
                    it.exception.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        })




        categoryViewModel.loading.observe(viewLifecycleOwner, Observer {

            viewDataBinding.layoutList.layoutList.isEnabled = !it

        })



        categoryViewModel.startQuizEvent.observe(viewLifecycleOwner, EventObserver {
            Timber.d("$it")

            val action =
                ChooseCategoryFragmentDirections.actionChooseCategoryToQuizFragment(it.toTypedArray())


            findNavController().navigate(action)


        })




        setHasOptionsMenu(true)


        return viewDataBinding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        removeImmersive()
    }


    fun removeImmersive() {
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.delete_all_score -> {


            }
            R.id.log_out -> {


                Toast.makeText(context, "Log out", Toast.LENGTH_SHORT).show()
                FirebaseAuth.getInstance().signOut()
                clearLoginDetails()
                findNavController().navigate(R.id.action_chooseCategory_to_signUpAndLogIn)

            }

            R.id.donate -> {

                findNavController().navigate(R.id.action_chooseCategory_to_donationFragment)
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
