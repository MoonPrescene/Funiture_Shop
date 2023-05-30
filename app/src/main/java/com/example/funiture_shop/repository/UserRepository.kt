package com.example.funiture_shop.repository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.funiture_shop.data.model.entity.User
import com.example.funiture_shop.data.model.res.Res
import com.example.funiture_shop.helper.SharedPreferencesHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

@Singleton
class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val db: FirebaseFirestore,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) {

    private val loginLiveData = MutableLiveData<Res>()
    private val signUpLiveData = MutableLiveData<Res>()
    private val updateImageLiveData = MutableLiveData<Res>()
    private val upImageUserLiveData = MutableLiveData<Res>()
    private val resetPasswordLiveData = MutableLiveData<Res>()
    private val createUserLiveData = MutableLiveData<Res>()

    fun signIn(email: String, pass: String): MutableLiveData<Res> {
        //this signInWithEmailAndPassword run default on main thread :v
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                loginLiveData.postValue(Res.Success(data = null))
            } else {
                loginLiveData.postValue(Res.Error(it.exception.toString()))
            }
        }
        return loginLiveData
    }

    fun signUp(email: String, pass: String): MutableLiveData<Res> {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                signUpLiveData.postValue(Res.Success(data = null))
            } else {
                signUpLiveData.postValue(Res.Error(it.exception.toString()))
            }
        }
        return signUpLiveData
    }

    private fun addAvatarToStorage(image: Uri): MutableLiveData<Res> {
        val storageRef = storage.reference

        val fileName = sharedPreferencesHelper.getUserName()
        val imagesRef = storageRef.child("images/users/$fileName")

        val uploadTask = imagesRef.putFile(image)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
                updateImageLiveData.postValue(Res.Error(task.exception.toString()))
            }
            imagesRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result
                updateImageLiveData.postValue(Res.Success(downloadUrl))
            } else {
                updateImageLiveData.postValue(Res.Error(task.exception.toString()))
            }
        }
        return updateImageLiveData
    }


    private fun addAvatarUrlToDB(image: String): MutableLiveData<Res> {
        val collectionRef = db.collection("users")
        collectionRef.document(sharedPreferencesHelper.getUserName())
            .update("imgUrl", image).addOnCompleteListener {
                if (it.isSuccessful) {
                    upImageUserLiveData.postValue(Res.Success(data = null))
                } else {
                    updateImageLiveData.postValue(Res.Error(it.exception.toString()))
                }
            }
        return upImageUserLiveData
    }

    fun resetPassword(): MutableLiveData<Res> {
        auth.sendPasswordResetEmail(sharedPreferencesHelper.getUserName())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resetPasswordLiveData.postValue(Res.Success(data = null))
                } else {
                    resetPasswordLiveData.postValue(Res.Error(task.exception.toString()))
                }
            }
        return resetPasswordLiveData
    }

    fun createUser(user: User): MutableLiveData<Res> {
        db.collection("users").document(sharedPreferencesHelper.getUserName()).set(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    createUserLiveData.postValue(Res.Success(data = null))
                } else {
                    createUserLiveData.postValue(Res.Error(it.exception.toString()))
                }
            }
        return createUserLiveData
    }
}