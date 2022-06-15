package com.io.appsfactorytesttask.views.splashscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.io.appsfactorytesttask.databinding.FragmentSplashScreenBinding
import com.io.appsfactorytesttask.utilities.showFragmentWithNoBack
import com.io.appsfactorytesttask.views.mainScreen.MainScreenFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashScreenFragment : Fragment() {
    private var binding: FragmentSplashScreenBinding? = null
    private val splashScreenBinding get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        GlobalScope.launch(Dispatchers.Main) {
            YoYo.with(Techniques.Bounce)
                .duration(1000)
                .repeat(2)
                .playOn(splashScreenBinding.imageAppsFactory)

            delay(2000)
            showFragmentWithNoBack(activity as AppCompatActivity, MainScreenFragment())
        }

        return splashScreenBinding.root

    }


}