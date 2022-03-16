package com.efrinaldi.zwallet.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.ui.layout.auth.AuthActivity
import com.efrinaldi.zwallet.ui.layout.main.MainActivity
import com.efrinaldi.zwallet.utils.KEY_LOGGED_IN
import com.efrinaldi.zwallet.utils.PREFS_NAME

@SuppressLint("CustomSplashScreen")
class SplashScreenAcitvity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen_acitvity)

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        if(prefs.getBoolean(KEY_LOGGED_IN, false)){
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    } else {
            Handler().postDelayed({
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)

        }
    }
}