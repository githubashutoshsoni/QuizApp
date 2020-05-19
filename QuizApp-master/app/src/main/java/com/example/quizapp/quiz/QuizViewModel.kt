package com.example.quizapp.quiz

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.quizapp.model.ResponseCategoryJson
import kotlinx.coroutines.launch

class QuizViewModel(app: Application) : AndroidViewModel(app) {


    private var _questionNumber: MutableLiveData<Int> =
        MutableLiveData(0)

    private var _completedQuiz: MutableLiveData<Boolean> = MutableLiveData(false)

    val completedQuiz: LiveData<Boolean> = _completedQuiz

    val questionNumber: LiveData<Int> = _questionNumber

    lateinit var quizItems: ArrayList<ResponseCategoryJson>


    val quizRepository = QuizRepository(app)

    val categoryList: LiveData<ArrayList<ResponseCategoryJson>> = quizRepository._categoryList

    fun onCategoryClicked(id: Int) {

        quizRepository.onClickCataegory(id)

    }


    fun setQuizObject(QuizObject: ArrayList<ResponseCategoryJson>) {

        quizItems = QuizObject

    }

    private var _question: MutableLiveData<ResponseCategoryJson> = MutableLiveData(emptyLIst())
    var question: LiveData<ResponseCategoryJson> = _question


    fun setValueOfPosition(questionPos: Int) {
        i = questionPos
        _questionNumber.value = i
        _question.value = quizItems[i]


    }

    fun onNextItemClicked() {

        i += 1
        _questionNumber.value = i
        if (quizItems.size > i) {

            _question.value = quizItems[i]
        } else {
            _completedQuiz.value = true

        }


    }


    fun nukeTable() {


        viewModelScope.launch {

            quizRepository.nuke()

        }

    }


    fun insertScore(score: Int) {
        viewModelScope.launch {

            quizRepository.insertScore(score)

        }
    }

    val TAG = QuizViewModel::class.java.simpleName

    var i = -1

    fun emptyLIst(): ResponseCategoryJson {
        return ResponseCategoryJson()
    }


}