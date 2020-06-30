package com.example.quizapp

import androidx.fragment.app.Fragment
import com.example.quizapp.Retrofit.RestApi
import com.example.quizapp.util.AppExecutors
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as QuizApplication).quizRepository
    return ViewModelFactory(repository, AppExecutors(), RestApi())
}

fun String.decodeUTF(): String {

    return try {
        var prevURL = ""
        var decodeURL = this
        while (prevURL != decodeURL) {
            prevURL = decodeURL
            decodeURL = URLDecoder.decode(decodeURL, "UTF-8")
        }
        decodeURL
    } catch (e: UnsupportedEncodingException) {
        "Issue while decoding" + e.stackTrace
    }


}

fun MutableList<String>.decodeUTF(): MutableList<String> {

    val iterator = this.listIterator()

    while (iterator.hasNext()) {
        val oldvalue = iterator.next()
        val newValue = oldvalue.decodeUTF()
        iterator.set(newValue)
    }

    return this
}
