package com.example.quizapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.quizapp.model.ResCategory
import com.example.quizapp.model.ResponseCategoryJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.android.synthetic.main.fragment_choose_category.view.*
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_choose_category, container, false)

        chooseCategoryViewModel = ViewModelProvider(activity!!).get(QuizViewModel::class.java)
        view.apply {


            collapasing_toolbar.title = "Select Category"
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
            }


            entertainment_btn.setOnClickListener {
                RestApi().apply {
                    openTriviaApi.getResponseData().enqueue(ResponseBody())

                }
            }

        }


        return view
    }

    companion object {
        const val QUIZ_KEY = "QUIZ_KEY"
    }

    val TAG = ChooseCategory::class.java.simpleName

    inner class ResponseBody : retrofit2.Callback<okhttp3.ResponseBody> {
        override fun onFailure(call: Call<okhttp3.ResponseBody>, t: Throwable) {
            Log.d("onFailure", "response failed");
        }

        override fun onResponse(
            call: Call<okhttp3.ResponseBody>,
            response: Response<okhttp3.ResponseBody>
        ) {

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
