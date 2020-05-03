package com.pmovil.webservices

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class AppUtils {
    companion object {
        fun showSnackbar(view: View?, message: String) {
            view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }
        }

        fun showSnackbarCloseable(view: View?, message: String) {
            view?.let {
                Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
                    .setAction("CLOSE", {})
                    .show()
            }
        }

        fun showToast(context: Context?, message: String) {
            context?.let { Toast.makeText(it, message, Toast.LENGTH_SHORT).show() }
        }

    }
}