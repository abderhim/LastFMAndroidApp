package com.io.appsfactorytesttask.utilities

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.io.appsfactorytesttask.databinding.ErrorDialogBinding
import com.io.appsfactorytesttask.databinding.LoadingBinding




fun createLoadingPopup(context: Context, message: String): AlertDialog {
    val  dialogMainBinding=LoadingBinding.inflate(LayoutInflater.from(context))
    val popupLoading = AlertDialog.Builder(context).create()
    popupLoading.setView(dialogMainBinding.root)
    popupLoading.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    popupLoading.setCancelable(false)
    dialogMainBinding.loadingText?.text = message
    return popupLoading
}


fun createErrorPopup(context: Context, message: String): AlertDialog {
    val  errorDialogBinding=ErrorDialogBinding.inflate(LayoutInflater.from(context))
    val errorPopup = AlertDialog.Builder(context).create()
    errorPopup.setView(errorDialogBinding.root)
    errorPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    errorPopup.setCancelable(true)
    errorDialogBinding.errorText?.text = message
    errorDialogBinding.cancelButton.setOnClickListener {
        errorPopup.dismiss()
    }
    return errorPopup
}
