package com.io.appsfactorytesttask.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.lifecycle.ViewModelProvider
import com.io.appsfactorytesttask.R
import com.io.appsfactorytesttask.utilities.showFragmentWithNoBack
import com.io.appsfactorytesttask.viewModels.MainScreenVM
import com.io.appsfactorytesttask.views.splashscreen.SplashScreenFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainScreenVM: MainScreenVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainScreenVM = ViewModelProvider(this).get(MainScreenVM::class.java)
        if(savedInstanceState==null){
            showFragmentWithNoBack(this,SplashScreenFragment())
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }


}