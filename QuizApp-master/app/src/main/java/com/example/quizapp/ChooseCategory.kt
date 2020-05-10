package com.example.quizapp

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R2.attr.menu
import com.example.quizapp.model.ResCategory
import com.example.quizapp.model.ResponseCategoryJson
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.android.synthetic.main.fragment_choose_category.view.*
import kotlinx.android.synthetic.main.layout_list.*
import kotlinx.android.synthetic.main.layout_list.view.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class ChooseCategory : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    lateinit var chooseCategoryViewModel: QuizViewModel
    lateinit var progressDoalog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_choose_category, container, false)

        chooseCategoryViewModel = ViewModelProvider(activity!!).get(QuizViewModel::class.java)
        view.apply {


            collapasing_toolbar.title = "Categories"
            collapasing_toolbar.setExpandedTitleColor(
                ResourcesCompat.getColor(resources, android.R.color.white, null)
            )

            collapasing_toolbar.setCollapsedTitleTextColor(
                ResourcesCompat.getColor(
                    resources,
                    android.R.color.black,
                    null
                )
            )



            if (activity is MainActivity) {

                (activity as MainActivity).setSupportActionBar(tool_bar)
//                (activity as MainActivity).supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
            }


            sport_btn.setOnClickListener {
                onClickCataegory(it.id)
            }

            myth_btn.setOnClickListener {
                onClickCataegory(it.id)
            }

            entertainment_btn.setOnClickListener {
                onClickCataegory(it.id)
            }


        }

        setHasOptionsMenu(true)


        return view
    }


    fun onClickCataegory(id: Int) {

        RestApi().apply {


            when (id) {
                R.id.entertainment_btn -> openTriviaApi.getResponseData(10)
                    .enqueue(ResponseBody())
                R.id.sport_btn -> openTriviaApi.getResponseData(21).enqueue(ResponseBody())
                R.id.myth_btn -> openTriviaApi.getResponseData(20).enqueue(ResponseBody())
            }


            progressDoalog = ProgressDialog(context);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Loading...");
            progressDoalog.setTitle("Loading your questions");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDoalog.show();


        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.all_score -> Toast.makeText(context, "all scores", Toast.LENGTH_SHORT).show()
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


    val TAG = ChooseCategory::class.java.simpleName

    inner class ResponseBody : retrofit2.Callback<okhttp3.ResponseBody> {


        override fun onFailure(call: Call<okhttp3.ResponseBody>, t: Throwable) {
            Log.d("onFailure", "response failed");
            progressDoalog.progress = 100
            progressDoalog.dismiss()
            Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show()
        }

        override fun onResponse(
            call: Call<okhttp3.ResponseBody>,
            response: Response<okhttp3.ResponseBody>
        ) {

            progressDoalog.progress = 100

            progressDoalog.dismiss()
            if (response.isSuccessful) {

                Log.d(TAG, "response successful")

                try {
                    val jsonString = response.body()?.string()

                    Log.d(TAG, "JSONString $jsonString")

                    val jsonObject: JSONObject = JSONObject(jsonString!!);
                    val categoryString: JSONArray = jsonObject.getJSONArray("results");
                    Log.d(TAG, categoryString.toString())

                    val type =
                        Types.newParameterizedType(
                            List::class.java,
                            ResponseCategoryJson::class.java
                        )


                    val moshi: Moshi = Moshi.Builder().add(QuizMoAdapter()).build()

                    val adapter = moshi.adapter<List<ResponseCategoryJson>>(type)
                    val arrayList: List<ResponseCategoryJson>? =
                        adapter?.fromJson(categoryString.toString())


                    chooseCategoryViewModel.setQuizObject(arrayList as ArrayList<ResponseCategoryJson>);



                    Navigation.findNavController(view!!)
                        .navigate(R.id.action_chooseCategory_to_quizFragment)

                    Log.d("MainActivity", "categories are" + arrayList?.get(0)?.question)
                    val body = "response.body()?.question"
                    Log.d("MainActivity", "response of body is$body")

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }

            } else
                Toast.makeText(context, "Error fetching details sorry", Toast.LENGTH_LONG).show();
        }

    }


}
