package com.example.funiture_shop.ui.authentication.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.funiture_shop.R
import com.example.funiture_shop.data.model.entity.LoginFormState
import com.example.funiture_shop.data.model.entity.User
import com.example.funiture_shop.data.model.res.Res
import com.example.funiture_shop.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm
    private val _signUpInfo: MutableLiveData<SignInViewModel.LoginInfo> = MutableLiveData()
    val loginInfo: LiveData<SignInViewModel.LoginInfo>
        get() = _signUpInfo

    private val _createUserInfo: MutableLiveData<User> = MutableLiveData()
    val createUserInfo: LiveData<User>
        get() = _createUserInfo

    val signInRes: LiveData<Res> = _signUpInfo.switchMap {
        userRepository.signUp(it.username, it.password)
    }

    fun signUp(username: String, password: String) {
        val loginInfo = SignInViewModel.LoginInfo(username, password)
        _signUpInfo.value = loginInfo
    }

    val createUserRes: LiveData<Res> = _createUserInfo.switchMap {
        userRepository.createUser(user = it)
    }

    fun createUser(user: User){
        _createUserInfo.value = user
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
}