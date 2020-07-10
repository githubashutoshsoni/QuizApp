package com.style.quiztrivia.ui.quiz

import androidx.lifecycle.*
import com.style.quiztrivia.util.Result
import com.style.quiztrivia.database.ResultQuiz
import com.style.quiztrivia.database.UserModel
import com.style.quiztrivia.repository.UserRepository
import com.style.quiztrivia.util.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    private val _progres: MutableLiveData<Int> = MutableLiveData(0)

    val progress: LiveData<Int> = _progres


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

    private val _correctScoreEvent: MutableLiveData<Event<Unit>> = MutableLiveData()
    val correctScoreEvent: LiveData<Event<Unit>> = _correctScoreEvent


    val _correctAnswer: MutableLiveData<Event<String>> = MutableLiveData()
    val correctAnwer: LiveData<Event<String>> = _correctAnswer

    private val _noOptionsSelectedEvent: MutableLiveData<Event<Unit>> = MutableLiveData()
    val noOptionsSelected: LiveData<Event<Unit>> = _noOptionsSelectedEvent
    var previousAnswer = ""

    fun checkAnswer(selectedAnwer: String) {
        val correct_ans = quizListItems[questionNumber].correct_answer

        if (selectedAnwer.isEmpty() || selectedAnwer == previousAnswer) {
            _noOptionsSelectedEvent.postValue(Event(Unit))
            return
        }

//        update the score if it is a correct selected answer
        if (selectedAnwer == correct_ans) {
            var score: Int = _score.value ?: 0
            score += 1
            _score.value = score
            _correctScoreEvent.value = Event(Unit)
        } else {
            _correctAnswer.value = Event(correct_ans)
        }
//        save the instance of previous answer
        previousAnswer = selectedAnwer
        onNextClicked()
    }


    val _roatateEvent: MutableLiveData<Event<Unit>> = MutableLiveData()
    val rotateEvent: LiveData<Event<Unit>> = _roatateEvent

    fun onNextClicked() {

        viewModelScope.launch {


            if (firstTime)
                questionNumber = 0
            else
                ++questionNumber


            if (questionNumber != 0) {
                _roatateEvent.value = Event(Unit)
                delay(800)
            }


            _questionNumber.value = questionNumber + 1
            _progres.value = (((questionNumber.toDouble()) / questionLength) * 100).toInt()

            firstTime = false

            if (questionNumber < quizListItems.size) {

                val current = quizListItems[questionNumber]
                resultQuiz.value = current
                _question.value = current.question
                _listOptions.value = current.incorrect_answers


            } else {

                _finalScoreEvent.value = Event(_score.value.toString())

            }

        }
    }


}