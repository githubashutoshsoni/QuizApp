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
import com.style.quiztrivia.MainActivity
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

class DonationFragment : Fragment() {

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


        }
        setupSnackbar()


        return binding.root
    }

    lateinit var userDetails: UserModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.lifecycleOwner = viewLifecycleOwner

        binding.enterAmt.post {
            binding.enterAmt.requestFocus()
            binding.enterAmt.showKeyboard()
        }


        lifecycleScope.launch {
            donateViewModel.getUserDetails().observe(viewLifecycleOwner, Observer {
                userDetails = it

            })
        }

        donateViewModel.checkoutLive.observe(viewLifecycleOwner, EventObserver { price ->

            if (activity is MainActivity) {
                (activity as MainActivity).startPayment(price, userDetails)
            }

        })


    }


    private fun setupSnackbar() {
        view?.setupSnackbar(
            viewLifecycleOwner,
            donateViewModel.snackBarError,
            Snackbar.LENGTH_SHORT
        )

    }


}