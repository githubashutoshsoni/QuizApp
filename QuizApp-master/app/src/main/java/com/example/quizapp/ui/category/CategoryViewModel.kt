package com.example.quizapp.ui.ui.category

import androidx.lifecycle.*
import com.example.quizapp.Retrofit.RestApi
import com.example.quizapp.database.ResponseModel
import com.example.quizapp.database.Result
import com.example.quizapp.database.ResultQuiz
import com.example.quizapp.decodeUTF
import com.example.quizapp.ui.quiz.QuizRepository
import com.example.quizapp.util.AppExecutors
import com.example.quizapp.util.Event
import java.lang.Exception

class CategoryViewModel(
    private val quizRepository: QuizRepository,
    private val appExecutors: AppExecutors,
    private val restApi: RestApi
) : ViewModel() {


    private val mutableQuizList: MutableLiveData<Result<ResponseModel>> = MutableLiveData()

    val quizList: LiveData<Result<ResponseModel>> = mutableQuizList

    val _startQuizEvent: MutableLiveData<Event<ArrayList<ResultQuiz>>> = MutableLiveData()

    val startQuizEvent: LiveData<Event<ArrayList<ResultQuiz>>> = _startQuizEvent


    fun fetchQuizItem(categoryId: Int) {


        appExecutors.networkIO().execute {

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
            }

        }

    }


}