package com.style.quiztrivia

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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


fun EditText.showKeyboard() {

//    isFocusableInTouchMode = true
//
//    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
//    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)

}

fun EditText.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

 fun ObjectAnimator.disableViewDuringAnimation(view: View) {

    // This extension method listens for start/end events on an animation and disables
    // the given view for the entirety of that animation.

    addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            view.isEnabled = false
        }

        override fun onAnimationEnd(animation: Animator?) {
            view.isEnabled = true
        }
    })
}

