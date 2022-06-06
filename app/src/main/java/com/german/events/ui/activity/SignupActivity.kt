package com.german.events.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.german.events.databinding.ActivitySignupBinding
import com.german.events.ui.viewmodel.SignupViewModel
import com.german.events.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewmodel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.optSignIn.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish()
        }

        binding.optSignUp.setOnClickListener{
            startActivity(Intent(this@SignupActivity, MainActivity::class.java))
            finish()
        }

        viewmodel.signupStatus.observe(this) { status ->
            when(status){
                Resource.Status.SUCCESS -> {
                    startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                    finish()
                }
                Resource.Status.ERROR -> {

                }
                Resource.Status.LOADING -> {

                }
            }
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
        super.onBackPressed()
    }
}