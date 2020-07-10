package com.style.quiztrivia.ui.category

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.style.quiztrivia.database.CategoryModel
import com.style.quiztrivia.retrofit.RestApi
import com.style.quiztrivia.database.ResponseModel
import com.style.quiztrivia.util.Result
import com.style.quiztrivia.database.ResultQuiz
import com.style.quiztrivia.decodeUTF
import com.style.quiztrivia.repository.UserRepository
import com.style.quiztrivia.util.AppExecutors
import com.style.quiztrivia.util.Event
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception


/**
 * A viewmodel to move to select a category and
 *
 */
class CategoryViewModel(
    private val userRepository: UserRepository,
    private val appExecutors: AppExecutors,
    private val restApi: RestApi
) : ViewModel() {


    private val _loadingData: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loadingData


    val clickable: LiveData<Boolean> = loading.map { !it }

    private val mutableQuizList: MutableLiveData<Result<ResponseModel>> = MutableLiveData()

    val quizList: LiveData<Result<ResponseModel>> = mutableQuizList

    private val _startQuizEvent: MutableLiveData<Event<ArrayList<ResultQuiz>>> = MutableLiveData()

    val startQuizEvent: LiveData<Event<ArrayList<ResultQuiz>>> = _startQuizEvent

    private val _startDonationEvent: MutableLiveData<Event<Unit>> = MutableLiveData()
    val startDonationEvent: LiveData<Event<Unit>> = _startDonationEvent

    private val _startLogoutEvent: MutableLiveData<Event<Unit>> = MutableLiveData()

    val startLogoutEvent: LiveData<Event<Unit>> = _startLogoutEvent

    private val _list: MutableLiveData<List<CategoryModel>> = MutableLiveData()
    val list = _list

    init {

        //todo move this to remote repository and fetch from default repository
        val list: List<CategoryModel> = mutableListOf(

            CategoryModel("https://placeimg.com/500/500/knowledge", 9, "General Knowledge"),
            CategoryModel("https://placeimg.com/500/500/Books", 10, "Entertainment Books"),
            CategoryModel(
                "https://placeimg.com/500/500/Television",
                14,
                "Entertainment Television"
            ),
            CategoryModel("https://placeimg.com/500/500/Science", 17, "Science and Nature"),
            CategoryModel("https://placeimg.com/500/500/Computers", 18, "Computers"),
            CategoryModel("https://placeimg.com/500/500/Geography", 22, "Geography"),
            CategoryModel("https://placeimg.com/500/500/History", 23, "History"),
            CategoryModel("https://placeimg.com/500/500/Animals", 27, "Animals"),
            CategoryModel("https://placeimg.com/500/500/Manga", 31, "Japanese Manga"),
            CategoryModel("https://placeimg.com/500/500/Cartoon", 32, "Cartoon Animation"),
            CategoryModel("https://placeimg.com/500/500/vehicles", 28, "Vehicles")

        )
        _list.postValue(list)
    }


    fun logout() {


        viewModelScope.launch {

            userRepository.nukeDetails()
            FirebaseAuth.getInstance().signOut()
            _startLogoutEvent.value = Event(Unit)
        }
    }

    fun dontate() {
        _startDonationEvent.value = Event(Unit)
    }


    /**
     *[categoryId] is the type of category.
     */
    fun fetchQuizItem(categoryId: Int) {

        _loadingData.postValue(true)

        Timber.d("fetching category $categoryId")


        if (_loadingData.value == false)
            appExecutors.networkIO().execute {

                mutableQuizList.postValue(Result.Loading)
                try {
                    val response = restApi.quizApiService.getResponseData(categoryId).execute()
                    if (response.isSuccessful && response.body() != null && response.body()
                            .toString()
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