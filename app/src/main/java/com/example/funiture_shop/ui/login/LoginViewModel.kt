package com.example.funiture_shop.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.switchMap
import com.example.funiture_shop.repository.LoginRepository
import com.example.funiture_shop.R
import com.example.funiture_shop.data.model.res.SignInRes
import com.example.funiture_shop.data.model.entity.LoginFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
    ) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginInfo: MutableLiveData<LoginInfo> = MutableLiveData()
    val loginInfo: LiveData<LoginInfo>
        get() = _loginInfo

    val signInRes: LiveData<SignInRes> = _loginInfo.switchMap { loginInfo ->
        loginRepository.signIn(loginInfo.username, loginInfo.password)
    }

    fun login(username: String, password: String) {
        val loginInfo = LoginInfo(username, password)
        _loginInfo.value = loginInfo
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    data class LoginInfo(val username: String, val password: String)
}