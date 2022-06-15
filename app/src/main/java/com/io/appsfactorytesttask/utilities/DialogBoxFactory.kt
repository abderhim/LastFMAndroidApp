package com.io.appsfactorytesttask.utilities

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.io.appsfactorytesttask.R

fun createLoadingPopup(context: Context, message: String): AlertDialog {
    val factory = LayoutInflater.from(context)
    val layoutView: View = factory.inflate(R.layout.loading, null)
    val popupLoading = AlertDialog.Builder(context).create()
    popupLoading.setView(layoutView)
    popupLoading.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    popupLoading.setCancelable(false)
    val loadingText = layoutView.findViewById<TextView>(R.id.loading_text)
    loadingText?.text = message
    return popupLoading
}

fun createErrorPopup(context: Context, message: String): AlertDialog {
    val factory = LayoutInflater.from(context)
    val layoutView: View = factory.inflate(R.layout.error_dialog, null)
    val errorPopup = AlertDialog.Builder(context).create()
    errorPopup.setView(layoutView)
    errorPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    errorPopup.setCancelable(true)
    val cancelButton = layoutView.findViewById<Button>(R.id.cancel_button)
    val errorText = layoutView.findViewById<TextView>(R.id.error_text)
    errorText?.text = message
    cancelButton.setOnClickListener {
        errorPopup.dismiss()
    }
    return errorPopup
}


