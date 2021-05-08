package com.example.covidtracker.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import com.example.covidtracker.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME = 2500


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        val textAnim = AnimationUtils.loadAnimation(this, R.anim.splash_anim)
        val imgAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        logo.animation = imgAnim
        logo.animation.duration = 2000

        app_name.animation = textAnim
        app_name.animation.duration = 2000


        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.slide_down_anim)
        }, SPLASH_TIME.toLong())
    }
}