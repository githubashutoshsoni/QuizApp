package com.example.quizapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.quizapp.model.ResponseCategoryJson
import kotlin.collections.ArrayList

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private var quizObject: ArrayList<ResponseCategoryJson>? = null

    private var _categoryList: MutableLiveData<ArrayList<ResponseCategoryJson>> =
        MutableLiveData(ArrayList());

    val categoryList: LiveData<ArrayList<ResponseCategoryJson>> = _categoryList

    private var _questionNumber: MutableLiveData<Int> =
        MutableLiveData(0)

    private var _completedQuiz: MutableLiveData<Boolean> = MutableLiveData(false)

    val completedQuiz: LiveData<Boolean> = _completedQuiz

    val questionNumber: LiveData<Int> = _questionNumber

    lateinit var quizItems: ArrayList<ResponseCategoryJson>

    fun setQuizObject(QuizObject: ArrayList<ResponseCategoryJson>) {
        this._categoryList.value = QuizObject
        quizItems = QuizObject
    }

    //    private var question: LiveData<ResponseCategoryQuiz>
    private var _question: MutableLiveData<ResponseCategoryJson> = MutableLiveData(emptyLIst())
    var question: LiveData<ResponseCategoryJson> = _question


    fun onNextItemClicked() {
        i++
        _questionNumber.value = i + 1
        if (quizItems.size > i) {

            Log.d(TAG, "Quesitoin is $quizItems[i].question ")
            _question.value = quizItems[i]

        } else {

            _completedQuiz.value = true

            Log.d(TAG, " questions finished")
        }


    }


    fun setCompletedToFalse() {
        _completedQuiz.value = false
    }

    val TAG = QuizViewModel::class.java.simpleName

    var i = -1

    fun emptyLIst(): ResponseCategoryJson {
        return ResponseCategoryJson()
    }

    fun getQuestionNumber() {


    }


}