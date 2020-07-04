package com.style.quiztrivia.ui.quiz

import androidx.lifecycle.*
import com.style.quiztrivia.database.Result
import com.style.quiztrivia.database.ResultQuiz
import com.style.quiztrivia.database.UserModel
import com.style.quiztrivia.util.AbsentLiveData
import com.style.quiztrivia.util.Event

class QuizViewModel(repository: UserRepository) : ViewModel() {


    val userName: LiveData<UserModel> =
        repository.observeUser().switchMap { getUserDetails(it) }


    fun getUserDetails(userResult: Result<UserModel>): LiveData<UserModel> {


        val result = MutableLiveData<UserModel>()

        if (userResult is Result.Success) {
            result.value = userResult.data

        } else {

            result.value = UserModel(userName = "Anonymous")

        }

        return result
    }


    private val _question: MutableLiveData<String> = MutableLiveData<String>()

    val question: LiveData<String> = _question

    private val _listOptions: MutableLiveData<MutableList<String>> = MutableLiveData()

    val listOptions: LiveData<MutableList<String>> = _listOptions

    var questionNumber = 0

    var _questionNumber: MutableLiveData<Int> = MutableLiveData()


    val _finalScoreEvent: MutableLiveData<Event<String>> = MutableLiveData()

    val finalScoreEvent: LiveData<Event<String>> = _finalScoreEvent


    private val resultQuiz: MutableLiveData<ResultQuiz> = MutableLiveData()

    lateinit var quizListItems: Array<ResultQuiz>

    var questionLength = 0

    fun setQuizList(quizList: Array<ResultQuiz>) {

        this.quizListItems = quizList

        questionLength = quizListItems.size
        onNextClicked()
    }


    private val selected_options: MutableLiveData<String> = MutableLiveData()
    var firstTime = true;

    private val _score: MutableLiveData<Int> = MutableLiveData(0)

    val score: LiveData<Int> = _score
    val _correctAnswer: MutableLiveData<Event<String>> = MutableLiveData()
    val correctAnwer: LiveData<Event<String>> = _correctAnswer

    fun checkAnswer(correct: String) {
        val correct_ans = quizListItems[questionNumber].correct_answer
        if (correct == correct_ans) {
            var score: Int = _score.value ?: 0
            score += 1
            _score.value = score
        } else {
            _correctAnswer.value = Event(correct_ans)
        }
        onNextClicked()
    }

    fun onNextClicked() {

        if (firstTime)
            questionNumber = 0
        else
            ++questionNumber

        _questionNumber.value = questionNumber

        firstTime = false

        if (questionNumber < quizListItems.size - 1) {

            val current = quizListItems[questionNumber]
            resultQuiz.value = current
            _question.value = current.question
            _listOptions.value = current.incorrect_answers


        } else {

            _finalScoreEvent.value = Event(_score.value.toString())

        }


    }


}