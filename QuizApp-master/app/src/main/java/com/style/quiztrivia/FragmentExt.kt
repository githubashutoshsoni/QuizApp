package com.style.quiztrivia

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.style.quiztrivia.retrofit.RestApi
import com.style.quiztrivia.util.AppExecutors
import com.style.quiztrivia.util.Event

import java.io.UnsupportedEncodingException
import java.net.URLDecoder

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as QuizApplication).userRepository
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

fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>,
    timeLength: Int
) {

    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            showSnackbar(context.getString(it), timeLength)
        }
    })
}

fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run {
        addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {

            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {

            }
        })
        show()
    }
}

