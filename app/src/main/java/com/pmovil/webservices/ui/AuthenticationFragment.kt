package com.pmovil.webservices.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.pmovil.webservices.AppUtils
import com.pmovil.webservices.R
import kotlinx.android.synthetic.main.fragment_authentication.view.*

class AuthenticationFragment : Fragment() {

    companion object {
        private const val USER_STATUS_IDENTIFIED = "User is Identified"
        private const val USER_STATUS_NOT_IDENTIFIED = "User is not Identified"
        private const val RC_SIGN_IN_GOOGLE = 100
        private const val RC_SIGN_IN_FIREBASE_UI = 200
    }

    private lateinit var mView: View

    private lateinit var firebaseAuth: FirebaseAuth
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_authentication, container, false)

        mView.create_user_email_password_btn.setOnClickListener { createUserEmailPassword() }
        mView.authenticate_email_password_btn.setOnClickListener { authenticationEmailPassword() }
        mView.sing_out_btn.setOnClickListener { signOut() }
        mView.authenticate_external_service_btn.setOnClickListener { authenticationExternalService() }
        mView.authenticate_anonymous_btn.setOnClickListener { authenticationAnonymous() }
        mView.authenticate_firebase_ui_btn.setOnClickListener { authenticationFirebaseUI() }

        firebaseAuth = FirebaseAuth.getInstance()
        updateUI(firebaseAuth.currentUser)

        initGoogleSignInClient()

        return mView
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            mView.user_display_name_txt.text = user.displayName
            mView.user_status_txt.text = USER_STATUS_IDENTIFIED
            mView.user_email_txt.text = user.email
            mView.user_uid_txt.text = user.uid
        } else {
            mView.user_display_name_txt.text = ""
            mView.user_status_txt.text = USER_STATUS_NOT_IDENTIFIED
            mView.user_email_txt.text = ""
            mView.user_uid_txt.text = ""
        }
    }

    private fun createUserEmailPassword() {
        if (!validateEmailAndPassword()) return

        val email = mView.user_email_input_txt.text.toString()
        val password = mView.user_password_input_txt.text.toString()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    AppUtils.showSnackbarCloseable(mView, "Created User with Email")
                    updateUI(firebaseAuth.currentUser)
                } else {
                    AppUtils.showSnackbarCloseable(mView, task.exception?.message.toString())
                    task.exception?.printStackTrace()
                    updateUI(null)
                }
            }
    }

    private fun authenticationEmailPassword() {
        if (!validateEmailAndPassword()) return

        val email = mView.user_email_input_txt.text.toString()
        val password = mView.user_password_input_txt.text.toString()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    AppUtils.showSnackbarCloseable(mView, "Signed In with Email")
                    updateUI(firebaseAuth.currentUser)
                } else {
                    AppUtils.showSnackbarCloseable(mView, task.exception?.message.toString())
                    task.exception?.printStackTrace()
                    updateUI(null)
                }
            }
    }

    private fun signOut() {
        firebaseAuth.signOut()
        updateUI(firebaseAuth.currentUser)
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
    }

    private fun authenticationExternalService() {
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                AppUtils.showSnackbarCloseable(mView, "Google sign in failed")
                e.printStackTrace()
            }
        }

        if (requestCode == RC_SIGN_IN_FIREBASE_UI) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {

                AppUtils.showSnackbarCloseable(mView, "Firebase UI with ${response?.providerType}")
                updateUI(firebaseAuth.currentUser)
            } else {
                AppUtils.showSnackbarCloseable(mView, "${response?.error?.message}")
                response?.error?.printStackTrace()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    AppUtils.showSnackbarCloseable(mView, "Signed In with Google")
                    updateUI(firebaseAuth.currentUser)
                } else {
                    AppUtils.showSnackbarCloseable(mView, task.exception?.message.toString())
                    task.exception?.printStackTrace()
                    updateUI(null)
                }
            }
    }

    private fun authenticationAnonymous() {
        firebaseAuth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    AppUtils.showSnackbarCloseable(mView, "Signed In Anonymously")
                    updateUI(firebaseAuth.currentUser)
                } else {
                    AppUtils.showSnackbarCloseable(mView, task.exception?.message.toString())
                    task.exception?.printStackTrace()
                    updateUI(null)
                }
            }
    }

    private fun authenticationFirebaseUI() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.AnonymousBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_logo)
                .build(),
            RC_SIGN_IN_FIREBASE_UI
        )
    }

    private fun validateEmailAndPassword(): Boolean {
        val email = mView.user_email_input_txt.text.toString()
        val password = mView.user_password_input_txt.text.toString()

        if (email.isEmpty()) {
            AppUtils.showToast(context, "Email can not be null")
            return false
        }

        if (password.isEmpty()) {
            AppUtils.showToast(context, "Password can not be null")
            return false
        }

        return true
    }
}