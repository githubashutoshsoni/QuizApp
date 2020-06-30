package com.example.quizapp.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R

class RateDialog : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        return activity?.let {

            val builder = AlertDialog.Builder(it)
            builder.setMessage("Rate us on Google Play")
                .setPositiveButton(
                    android.R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->


                        val appPackageName: String? =
                            context?.packageName // getPackageName() from Context or Activity object

                        try {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=$appPackageName")
                                )
                            )
                        } catch (anfe: ActivityNotFoundException) {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                                )
                            )

                        }
                        finally {
                            val action = R.id.action_rating_dialog_to_chooseCategory
                            findNavController().navigate(action)
                        }
                    })


            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->

                val action = R.id.action_rating_dialog_to_chooseCategory
                findNavController().navigate(action)

            })

            builder.create()
        } ?: throw IllegalStateException("activity can not be null")

    }

}


