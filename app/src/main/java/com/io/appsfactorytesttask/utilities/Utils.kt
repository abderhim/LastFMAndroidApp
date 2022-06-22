package com.io.appsfactorytesttask.utilities

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.io.appsfactorytesttask.R
import java.lang.Exception


fun showFragment(activity:AppCompatActivity,fragment: Fragment){
    val transaction = activity?.supportFragmentManager?.beginTransaction()
    transaction?.replace(R.id.fragment_main, fragment)?.addToBackStack(null)
    transaction?.commit()
}

fun showFragmentWithNoBack(activity:AppCompatActivity,fragment: Fragment){
    val transaction = activity?.supportFragmentManager?.beginTransaction()
    transaction?.replace(R.id.fragment_main, fragment)
    transaction?.commit()
}

fun verifyArtistName(artistName: String?): Boolean {
    return !artistName.isNullOrEmpty()

}


fun bitMapToString(bitmap: Bitmap?): String {
    val baos = ByteArrayOutputStream()
    bitmap?.compress(Bitmap.CompressFormat.PNG, 100, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}

fun stringToBitMap(encodedString: String?): Bitmap? {
    return try {
        val encodeByte =
            Base64.decode(encodedString, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    } catch (e: Exception) {
        e.message
        null
    }
}


