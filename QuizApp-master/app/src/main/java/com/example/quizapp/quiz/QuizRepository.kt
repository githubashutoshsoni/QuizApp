package com.example.quizapp.quiz

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.quizapp.model.CommonErrorModel
import com.example.quizapp.MoshiAdapter.QuizMoAdapter
import com.example.quizapp.R
import com.example.quizapp.Retrofit.RestApi
import com.example.quizapp.database.AppDatabase
import com.example.quizapp.database.ScoreModel
import com.example.quizapp.model.ResponseCategoryJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class QuizRepository(val context: Context) : CommonErrorModel() {


    fun onClickCataegory(id: Int) {

        updateLoading(true)

        RestApi().apply {

            when (id) {
                R.id.entertainment_btn -> openTriviaApi.getResponseData(10)
                    .enqueue(ResponseBody())
                R.id.sport_btn -> openTriviaApi.getResponseData(21).enqueue(ResponseBody())
                R.id.myth_btn -> openTriviaApi.getResponseData(20).enqueue(ResponseBody())
            }


        }
    }

    val TAG = "QuizRepo"
    var _categoryList: MutableLiveData<ArrayList<ResponseCategoryJson>> =
        MutableLiveData(ArrayList())

    inner class ResponseBody : retrofit2.Callback<okhttp3.ResponseBody> {


        override fun onFailure(call: Call<okhttp3.ResponseBody>, t: Throwable) {
            updateLoading(false)
            errorDialog(true, t, true)
        }

        override fun onResponse(
            call: Call<okhttp3.ResponseBody>,
            response: Response<okhttp3.ResponseBody>
        ) {


            if (response.isSuccessful) {

//                Log.d(TAG, "response successful")

                try {
                    val jsonString = response.body()?.string()

                    Log.d(TAG, "JSONString $jsonString")

                    val jsonObject: JSONObject = JSONObject(jsonString!!)
                    val categoryString: JSONArray = jsonObject.getJSONArray("results")
//                    Log.d(TAG, categoryString.toString())

                    val type =
                        Types.newParameterizedType(
                            List::class.java,
                            ResponseCategoryJson::class.java
                        )


                    val moshi: Moshi = Moshi.Builder().add(QuizMoAdapter()).build()

                    val adapter = moshi.adapter<List<ResponseCategoryJson>>(type)
                    val arrayList: List<ResponseCategoryJson>? =
                        adapter?.fromJson(categoryString.toString())


//                    chooseCategoryViewModel.setQuizObject(arrayList as ArrayList<ResponseCategoryJson>);


//                    Navigation.findNavController(view!!)
//                        .navigate(R.id.action_chooseCategory_to_quizFragment)
                    _categoryList.value = arrayList as ArrayList<ResponseCategoryJson>

                    Log.d("MainActivity", "categories are" + arrayList?.get(0)?.question)
                    val body = "response.body()?.question"
                    Log.d("MainActivity", "response of body is$body")

                } catch (e: Exception) {
                    e.printStackTrace()
                    errorDialog(show = true, isListEmpty = true, e = e)

                }


            } else {

                updateLoading(false)
                errorDialog(show = true, isListEmpty = true, e = null)

            }
//                Toast.makeText(context, "Error fetching details sorry", Toast.LENGTH_LONG).show();
        }

    }


    suspend fun insertScore(score: Int) {

        withContext(Dispatchers.IO) {

            AppDatabase.getDatabase(context).scoreDao().insert(ScoreModel(score = score))
        }

    }

    suspend fun nuke() {


        withContext(Dispatchers.IO) {

            val a = AppDatabase.getDatabase(context).scoreDao().nukeTable()
            Log.d("QuizRepo nuked ", a.toString())

        }


    }

    interface itemsClearedCallBack {
        fun clearedAmount(items: Int)
    }

}