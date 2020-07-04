package com.style.quiztrivia.ui.donate

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.style.quiztrivia.database.UserModel
import com.style.quiztrivia.databinding.FragmentDonationBinding
import com.style.quiztrivia.getViewModelFactory
import com.style.quiztrivia.setupSnackbar
import com.style.quiztrivia.showKeyboard
import com.style.quiztrivia.util.EventObserver
import kotlinx.android.synthetic.main.fragment_donation.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber

class DonationFragment : Fragment(), PaymentResultListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    val donateViewModel by viewModels<DonateViewModel> { getViewModelFactory() }

    lateinit var binding: FragmentDonationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDonationBinding.inflate(inflater, container, false).apply {
            viewmodel = donateViewModel


            enterAmt.post {
                enterAmt.requestFocus()
                enterAmt.showKeyboard()
            }

        }
        setupSnackbar()


        return binding.root
    }

    lateinit var userDetails: UserModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Checkout.preload(activity!!.applicationContext)

        binding.lifecycleOwner = viewLifecycleOwner

        lifecycleScope.launch {
            donateViewModel.getUserDetails().observe(viewLifecycleOwner, Observer {
                userDetails = it

            })
        }

        donateViewModel.checkoutLive.observe(viewLifecycleOwner, EventObserver { price ->
            startPayment(price)

        })


    }


    private fun startPayment(money: Int = 10000) {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */


        val activity: Activity = requireActivity()
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


    private fun setupSnackbar() {
        view?.setupSnackbar(
            viewLifecycleOwner,
            donateViewModel.snackBarError,
            Snackbar.LENGTH_SHORT
        )

    }

    override fun onPaymentError(p0: Int, p1: String?) {

        Timber.e("onErrorpayments exe")
        Toast.makeText(activity, "error $p0  $p1", Toast.LENGTH_LONG).show()

    }

    override fun onPaymentSuccess(p0: String?) {
        Timber.e("Payment Successfull")
        donateViewModel.successFullyPaid()
    }


}