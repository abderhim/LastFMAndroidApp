package com.io.appsfactorytesttask.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.io.appsfactorytesttask.R
import com.io.appsfactorytesttask.utilities.showFragmentWithNoBack
import com.io.appsfactorytesttask.viewModels.MainVM
import com.io.appsfactorytesttask.views.splashscreen.SplashScreenFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainVM: MainVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainVM = ViewModelProvider(this).get(MainVM::class.java)
        showFragmentWithNoBack(this,SplashScreenFragment())
    }





}