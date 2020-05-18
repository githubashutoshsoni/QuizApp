package com.example.quizapp

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.dynamicanimation.animation.SpringForce.DAMPING_RATIO_LOW_BOUNCY
import androidx.dynamicanimation.animation.SpringForce.STIFFNESS_LOW
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.quizapp.PrefHelper.clearLoginDetails
import com.example.quizapp.Retrofit.RestApi
import com.example.quizapp.model.ResponseCategoryJson
import com.example.quizapp.model.ScoreModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_choose_category.view.*
import kotlinx.android.synthetic.main.layout_list.view.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class ChooseCategoryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onStart() {
        super.onStart()

        if (PrefHelper.getUserDetails(activity!!).isNullOrEmpty()) {


            Navigation.findNavController(view!!)
                .navigate(R.id.action_chooseCategory_to_signUpAndLogIn)

        }


    }


    fun createSpringAnim(view: View, property: DynamicAnimation.ViewProperty): SpringAnimation {
        return SpringAnimation(view, property).setSpring(SpringForce().apply {
            stiffness = STIFFNESS_LOW
            dampingRatio = DAMPING_RATIO_LOW_BOUNCY
        })
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

            scroll_view.setOnScrollChangeListener { _, _, _, _, _ ->

                header.isSelected = scroll_view.canScrollVertically(-1)
            }


            if (activity is MainActivity) {

                (activity as MainActivity).setSupportActionBar(tool_bar)
//                (activity as MainActivity).supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
            }


//            sport_btn.setOnClickListener {
//                onClickCataegory(it.id)
//            }
//
//            myth_btn.setOnClickListener {
//                onClickCataegory(it.id)
//            }


            val firstXAnim = createSpringAnim(mat_btn_1, DynamicAnimation.X)
            val firstYAnim = createSpringAnim(mat_btn_1, DynamicAnimation.Y)
            val secondXAnim = createSpringAnim(content_progress_bar, DynamicAnimation.X)
            val secondYAnim = createSpringAnim(content_progress_bar, DynamicAnimation.Y)

            val viewYDistance = 0
            var dX = 0f
            var dY = 0f
            drag_me.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = view.x - event.rawX
                        dY = view.y - event.rawY
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val newX = event.rawX + dX
                        val newY = event.rawY + dY

                        view.animate().x(newX).y(newY).setDuration(0).start()
                        firstXAnim.animateToFinalPosition(newX)
                        firstYAnim.animateToFinalPosition(newY + viewYDistance)
                    }
                }

                return@setOnTouchListener true
            }

            mat_btn_1.setOnClickListener {
//                onClickCataegory(it.id)

            }


        }

        setHasOptionsMenu(true)


        return view
    }


    fun onClickCataegory(id: Int) {

        RestApi().apply {


            when (id) {
                R.id.mat_btn_1 -> openTriviaApi.getResponseData(categoryId = 10)
                    .enqueue(ResponseBody())
//                R.id.sport_btn -> openTriviaApi.getResponseData(categoryId = 21).enqueue(ResponseBody())
//                R.id.myth_btn -> openTriviaApi.getResponseData(categoryId = 20).enqueue(ResponseBody())
            }


            progressDoalog = ContentLoadingProgressBar(context!!);
            progressDoalog.show();


        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.delete_all_score -> {

                Realm.init(context)

                val realm = Realm.getDefaultInstance()

                realm.executeTransaction { realm ->
                    run {

                        val results = realm.where<ScoreModel>().findAll()

                        if (results.size > 0) {


                            val deleted = results.deleteAllFromRealm();
                            Toast.makeText(
                                context,
                                "all scores count ${results.size}  deleted $deleted",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            Toast.makeText(
                                context,
                                "0 deleted",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }


                    }
                }


            }
            R.id.log_out -> {

                Toast.makeText(context, "Log out", Toast.LENGTH_SHORT).show()

                clearLoginDetails(activity!!)
                findNavController().navigate(R.id.action_chooseCategory_to_signUpAndLogIn)

            }
        }

        return super.onOptionsItemSelected(item)
    }


    val TAG = ChooseCategoryFragment::class.java.simpleName

    inner class ResponseBody : retrofit2.Callback<okhttp3.ResponseBody> {


        override fun onFailure(call: Call<okhttp3.ResponseBody>, t: Throwable) {
            Log.d("onFailure", "response failed");
            progressDoalog.hide()
            Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show()
        }

        override fun onResponse(
            call: Call<okhttp3.ResponseBody>,
            response: Response<okhttp3.ResponseBody>
        ) {


            progressDoalog.hide()
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
