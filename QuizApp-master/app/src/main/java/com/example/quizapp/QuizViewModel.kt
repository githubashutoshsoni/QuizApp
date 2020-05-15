package com.example.quizapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.quizapp.model.ResponseCategoryJson

class QuizViewModel(application: Application) : AndroidViewModel(application) {


    private var _categoryList: MutableLiveData<ArrayList<ResponseCategoryJson>> =
        MutableLiveData(ArrayList());

    val categoryList: LiveData<ArrayList<ResponseCategoryJson>> = _categoryList

    private var _questionNumber: MutableLiveData<Int> =
        MutableLiveData(0)

    private var _completedQuiz: MutableLiveData<Boolean> = MutableLiveData(false)

    val completedQuiz: LiveData<Boolean> = _completedQuiz

    val questionNumber: LiveData<Int> = _questionNumber

    lateinit var quizItems: ArrayList<ResponseCategoryJson>

    private val QUESTIONS_KEY = "quesions"

//    lateinit var mState: SavedStateHandle;
//    fun SavedStateViewModel(savedStateHandle: SavedStateHandle) {
//        mState = savedStateHandle
//    }

    fun setQuizObject(QuizObject: ArrayList<ResponseCategoryJson>) {
        this._categoryList.value = QuizObject
        quizItems = QuizObject
    }

    //    private var question: LiveData<ResponseCategoryQuiz>
    private var _question: MutableLiveData<ResponseCategoryJson> = MutableLiveData(emptyLIst())
    var question: LiveData<ResponseCategoryJson> = _question


    //    for saving the instance of quesiton position...
    fun setValueOfPosition(questionPos: Int) {
        i = questionPos
        _questionNumber.value = i
        _question.value = quizItems[i]
    }

    fun onNextItemClicked() {

        i += 1
        _questionNumber.value = i
        if (quizItems.size > i) {
            Log.d(TAG, "Quesitoin is ${quizItems[i].question} ")
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


}