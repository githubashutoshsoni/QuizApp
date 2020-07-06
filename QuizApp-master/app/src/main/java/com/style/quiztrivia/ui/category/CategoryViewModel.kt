package com.style.quiztrivia.ui.category

import androidx.lifecycle.*
import bolts.Bolts
import com.google.firebase.auth.FirebaseAuth
import com.style.quiztrivia.retrofit.RestApi
import com.style.quiztrivia.database.ResponseModel
import com.style.quiztrivia.database.Result
import com.style.quiztrivia.database.ResultQuiz
import com.style.quiztrivia.decodeUTF
import com.style.quiztrivia.ui.quiz.UserRepository
import com.style.quiztrivia.util.AppExecutors
import com.style.quiztrivia.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception

class CategoryViewModel(
    private val userRepository: UserRepository,
    private val appExecutors: AppExecutors,
    private val restApi: RestApi
) : ViewModel() {


    private val _loadingData: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loadingData

    private val mutableQuizList: MutableLiveData<Result<ResponseModel>> = MutableLiveData()

    val quizList: LiveData<Result<ResponseModel>> = mutableQuizList

    private val _startQuizEvent: MutableLiveData<Event<ArrayList<ResultQuiz>>> = MutableLiveData()

    val startQuizEvent: LiveData<Event<ArrayList<ResultQuiz>>> = _startQuizEvent

    private val _startDonationEvent: MutableLiveData<Event<Unit>> = MutableLiveData()
    val startDonationEvent: LiveData<Event<Unit>> = _startDonationEvent

    private val _startLogoutEvent: MutableLiveData<Event<Unit>> = MutableLiveData()

    val startLogoutEvent: LiveData<Event<Unit>> = _startLogoutEvent

    fun logout() {


        viewModelScope.launch {

            FirebaseAuth.getInstance().signOut()
            userRepository.nukeDetails()
            _startLogoutEvent.value = Event(Unit)
        }
    }

    fun dontate() {
        _startDonationEvent.value = Event(Unit)
    }


    fun fetchQuizItem(categoryId: Int) {

        appExecutors.networkIO().execute {
            _loadingData.postValue(true)
            mutableQuizList.postValue(Result.Loading)
            try {
                val response = restApi.quizApiService.getResponseData(9).execute()
                if (response.isSuccessful && response.body() != null && response.body().toString()
                        .isNotEmpty()
                ) {

                    val responseModel: ResponseModel = response.body() as ResponseModel

                    mutableQuizList.postValue(Result.Success(responseModel))


                    for (item in responseModel.results) {


                        item.incorrect_answers.decodeUTF()
                        item.incorrect_answers.add(item.correct_answer.decodeUTF())
                        item.incorrect_answers.shuffle()

                    }


                    val iterator = responseModel.results.listIterator()


                    val list: ArrayList<ResultQuiz> = ArrayList()

                    while (iterator.hasNext()) {

                        val oldValue = iterator.next()
                        val decodeList = oldValue.incorrect_answers
                        val decodeQuestion = oldValue.question.decodeUTF()
                        val decodeAnswer = oldValue.correct_answer.decodeUTF()
                        val difficulty = oldValue.difficulty.decodeUTF()

                        val resultQuiz =
                            ResultQuiz(
                                oldValue.category.decodeUTF(),
                                decodeAnswer,
                                difficulty,
                                decodeList,
                                decodeQuestion,
                                oldValue.type.decodeUTF()
                            )

                        list.add(resultQuiz)


                    }

                    _startQuizEvent.postValue(Event(list))

                } else {
                    mutableQuizList.value =
                        Result.Error(Exception("Empty response error"))
                }

            } catch (e: Exception) {
                mutableQuizList.postValue(Result.Error(e))
            } finally {
                _loadingData.postValue(false)
            }

        }

    }


}