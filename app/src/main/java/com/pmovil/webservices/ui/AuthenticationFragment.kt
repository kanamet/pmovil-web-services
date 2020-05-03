package com.pmovil.webservices.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pmovil.webservices.R
import kotlinx.android.synthetic.main.fragment_authentication.view.*

class AuthenticationFragment : Fragment() {

    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_authentication, container, false)

        mView.create_user_email_password_btn.setOnClickListener { createUserEmailPassword() }
        mView.authenticate_email_password_btn.setOnClickListener { authenticationEmailPassword() }
        mView.authenticate_external_service_btn.setOnClickListener { authenticationExternalService() }
        mView.authenticate_anonymous_btn.setOnClickListener { authenticationAnonymous() }
        mView.authenticate_firebase_ui_btn.setOnClickListener { authenticationFirebaseUI() }

        return mView
    }

    private fun createUserEmailPassword() {
        // TODO Implement logic to Create a User with Email and Password
    }

    private fun authenticationEmailPassword() {
        // TODO Implement logic to Authenticated with Email and Password
    }

    private fun authenticationExternalService() {
        // TODO Implement logic to Authenticated with External Service
    }

    private fun authenticationAnonymous() {
        // TODO Implement logic to Authenticated as Anonymous
    }

    private fun authenticationFirebaseUI() {
        // TODO Implement logic to Authenticated with Firebase UI
    }

}