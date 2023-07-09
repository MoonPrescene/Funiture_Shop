package com.example.funiture_shop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.funiture_shop.common.showToast
import com.example.funiture_shop.databinding.ActivityLoginBinding
import com.example.funiture_shop.helper.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_sign_in_fragment) as NavHostFragment
        val navController = navHostFragment.navController

    }

    override fun onStart() {
        super.onStart()
        /*val sharedPreferencesHelper = SharedPreferencesHelper(this) // 'this' refers to the current context
        this.showToast(sharedPreferencesHelper.getUserName())*/
        if (sharedPreferencesHelper.getUserName().isNotBlank()) {
            val intent = Intent(
                this, MainActivity::class.java
            )
            startActivity(intent)
        }
    }
}