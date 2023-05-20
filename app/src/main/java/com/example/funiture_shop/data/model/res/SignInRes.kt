package com.example.funiture_shop.data.model.res

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class SignInRes {
    object Success : SignInRes()
    data class Error(val message: String) : SignInRes()
}