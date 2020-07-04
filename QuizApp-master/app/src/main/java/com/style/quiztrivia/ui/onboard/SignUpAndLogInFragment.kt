package com.style.quiztrivia.ui.onboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation

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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.style.quiztrivia.BuildConfig
import com.style.quiztrivia.R
import com.style.quiztrivia.database.UserModel
import com.style.quiztrivia.getViewModelFactory
import kotlinx.android.synthetic.main.fragment_sign_up_and_log_in.view.*


/**
 * A simple [Fragment] subclass.
 */
class SignUpAndLogInFragment : Fragment() {


    val callbackManager = CallbackManager.Factory.create();


    private lateinit var auth: FirebaseAuth

    val viewmodel by viewModels<LoginViewModel> { getViewModelFactory() }

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




        globalView.apply {

            val facebookBtn = facebook_login_btn.apply {
                fragment = this@SignUpAndLogInFragment
                setReadPermissions("email", "public_profile")
            }




            facebookBtn.setOnClickListener {


                facebookBtn.registerCallback(
                    callbackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onSuccess(result: LoginResult?) {
                            handleFacebookAccessToken(result!!.accessToken)
                        }

                        override fun onCancel() {

                        }

                        override fun onError(error: FacebookException?) {

                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                        }

                    })

            }


            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_KEY)
                .requestEmail()
                .build()


            val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
            val account = GoogleSignIn.getLastSignedInAccount(context)


            google_sign_in.setOnClickListener {


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


        if (!getUserDetails().isNullOrEmpty()) {
            Navigation.findNavController(globalView)
                .navigate(R.id.action_signUpAndLogIn_to_chooseCategory)
        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun handleFacebookAccessToken(token: AccessToken) {


        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's informatio
                    val user = auth.currentUser
                    setUpUserDetails(user)

                } else {
                    // If sign in fails, display a message to the user.

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


            // Signed in successfully, show authenticated UI.

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

        }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        activity?.let {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        setUpUserDetails(user)
                    } else {
                        Snackbar.make(view!!, "Authentication Failed.", Snackbar.LENGTH_SHORT)
                            .show()
                    }


                }
        }
    }

    fun getUserDetails(): String? {

        val userName = activity!!.getPreferences(Context.MODE_PRIVATE)
            ?.getString(getString(R.string.user_name), null)

        return userName
    }

    fun setUpUserDetails(user: FirebaseUser?) {


        val name: String = user?.displayName.toString()
        val phone: String = user?.phoneNumber.toString()
        val email: String = user?.email.toString()

        val usermodel: UserModel = UserModel(name, phone, email)
        viewmodel.setUpUserName(usermodel)


        Navigation.findNavController(globalView)
            .navigate(R.id.action_signUpAndLogIn_to_chooseCategory);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
//            Log.d(TAG, "RC_SIGN_IN")
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);


        super.onActivityResult(requestCode, resultCode, data)
    }

}
