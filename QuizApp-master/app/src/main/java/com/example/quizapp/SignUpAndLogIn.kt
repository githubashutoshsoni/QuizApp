package com.example.quizapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.quizapp.PrefHelper.setUpSHaredPreference
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_sign_up_and_log_in.view.*


/**
 * A simple [Fragment] subclass.
 */
class SignUpAndLogIn : Fragment() {


    val callbackManager = CallbackManager.Factory.create();
    val EMAIL = "email"


    val TAG = SignUpAndLogIn::class.java.simpleName

    private lateinit var auth: FirebaseAuth
// ...
// Initialize Firebase Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

    }

    lateinit var globalView: View;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        globalView = inflater.inflate(R.layout.fragment_sign_up_and_log_in, container, false)


        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired




        Log.d(TAG, "access token$accessToken")
        Log.d(TAG, "IsLogged in $isLoggedIn")

        globalView.apply {

            val facebookBtn = facebook_login_btn.apply {
                fragment = this@SignUpAndLogIn
                setReadPermissions("email", "public_profile")
            }




            facebookBtn.setOnClickListener {


                Log.d(TAG, "onclick fb")

                facebookBtn.registerCallback(
                    callbackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onSuccess(result: LoginResult?) {
                            Log.d(TAG, "Success")
                            handleFacebookAccessToken(result!!.accessToken)
                        }

                        override fun onCancel() {
                            Log.d(TAG, "canceled")
                        }

                        override fun onError(error: FacebookException?) {
                            Log.d(TAG, "error")
                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                        }

                    })

            }


            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("196273572552-6gm31bgbl5032kbiqbb2fqs02d9r8erd.apps.googleusercontent.com")
                .requestEmail()
                .build()


            val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
            val account = GoogleSignIn.getLastSignedInAccount(context)


            google_sign_in.setOnClickListener {

                Log.d(TAG, "google clicked")
                val signInIntent = mGoogleSignInClient.signInIntent

                startActivityForResult(signInIntent, RC_SIGN_IN)
            }


        }


        // Inflate the layout for this fragment
        return globalView
    }

    val RC_SIGN_IN = 101;

    override fun onStart() {
        super.onStart()


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")


                    val user = auth.currentUser
                    user?.displayName
                    activity?.getSharedPreferences("user_name", Context.MODE_PRIVATE);
                    setUpSHaredPreference(user!!.displayName.toString(), activity!!)
                    Navigation.findNavController(globalView)
                        .navigate(R.id.action_signUpAndLogIn_to_chooseCategory)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }


            }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)




            firebaseAuthWithGoogle(account?.idToken!!)

            Log.d(TAG, "Signin successful")
            // Signed in successfully, show authenticated UI.

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode())
        }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        activity?.let {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {


                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser

                        setUpSHaredPreference(user?.displayName ?: "Anonymous", it);


                        Navigation.findNavController(globalView)
                            .navigate(R.id.action_signUpAndLogIn_to_chooseCategory);
                        //                    updateUI(user)
                    } else {

                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        // ...

                        Snackbar.make(view!!, "Authentication Failed.", Snackbar.LENGTH_SHORT)
                            .show()
                        //                    updateUI(null)
                    }


                }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        Log.d(TAG, "Request code is $requestCode==$RC_SIGN_IN  Result code is $resultCode ")
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Log.d(TAG, "RC_SIGN_IN")
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);


        super.onActivityResult(requestCode, resultCode, data)
    }

}
