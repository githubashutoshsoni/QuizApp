package com.style.quiztrivia

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.style.quiztrivia.database.UserModel
import org.json.JSONObject
import timber.log.Timber


class MainActivity : AppCompatActivity(), PaymentResultListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Checkout.clearUserData(this)
        Checkout.preload(applicationContext)
    }


    fun startPayment(money: Int = 10000, userDetails: UserModel) {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */


        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
//            todo get the name here
            options.put("name", userDetails.userName)
            options.put(
                "description",
                "It will help me develop more application and keep me motivated!"
            )
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://img.icons8.com/cute-clipart/64/000000/happy.png")
            options.put("currency", "INR")
            options.put("amount", money.toString())

            val prefill = JSONObject()
            prefill.put("email", userDetails.email)
            prefill.put("contact", userDetails.mobileNumber)

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Timber.e("onErrorpayments exe")
        Toast.makeText(this, "error $p0  $p1", Toast.LENGTH_LONG).show()

    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Thanks, I really appreciate it", Toast.LENGTH_SHORT).show()
    }

}
