package com.pmovil.webservices.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        return mView
    }
/*
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
*/

    private fun createUserEmailPassword() {
        if (!validateEmailAndPassword()) return

        val email = mView.user_email_input_txt.text.toString()
        val password = mView.user_password_input_txt.text.toString()

        // TODO Implement logic to Create a User with Email and Password
    }

    private fun authenticationEmailPassword() {
        if (!validateEmailAndPassword()) return

        val email = mView.user_email_input_txt.text.toString()
        val password = mView.user_password_input_txt.text.toString()

        // TODO Implement logic to Authenticated with Email and Password
    }

    private fun signOut() {
        // TODO Implement logic to Sign Out
    }

    private fun initGoogleSignInClient() {
        // TODO Implement logic to init Google Sign In Client
    }

    private fun authenticationExternalService() {
        // TODO Implement logic to Authenticated with External Service
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

/*
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        // TODO Implement logic to Authenticated with Google
    }
*/

    private fun authenticationAnonymous() {
        // TODO Implement logic to Authenticated as Anonymous
    }

    private fun authenticationFirebaseUI() {
        // TODO Implement logic to Authenticated with Firebase UI
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