package com.example.funiture_shop.data.model.res

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Res {
    data class Success<T>(val data: T?) : Res()
    data class Error(val message: String) : Res()
}