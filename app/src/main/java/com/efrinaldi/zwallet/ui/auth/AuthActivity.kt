package com.efrinaldi.zwallet.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.databinding.ActivityAuthBinding


class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}