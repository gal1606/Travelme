package com.example.travelme.viewmodels

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travelme.CurrentUser
import com.example.travelme.bitmapToUrl
import com.example.travelme.models.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class FirebaseAuthVM: ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private var _email: String by mutableStateOf("")
    var email: String
        get() = _email
        set(value) {
            _email = value
        }

    private var _password: String by mutableStateOf("")
    var password: String
        get() = _password
        set(value) {
            _password = value
        }

    private var _name: String by mutableStateOf("")
    var name: String
        get() = _name
        set(value) {
            _name = value
        }

    fun updateProfile(
        bitmap: Bitmap?,
        name: String,
        onSuccess: (result: User) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        if (bitmap != null) {
            bitmapToUrl(
                bitmap = bitmap,
                path ="pics/",
                onSuccess = { result ->
                    val profileUpdates = userProfileChangeRequest {
                        displayName = name
                        photoUri = result
                    }
                    auth.currentUser!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                onSuccess(
                                    User(
                                        id = auth.currentUser?.uid ?: "",
                                        email = auth.currentUser?.email ?: "",
                                        name = name,
                                        profileImage = auth.currentUser?.photoUrl.toString()
                                    )
                                )
                            }
                        }
                },
                onFailure = {}
            )
        }
        else
        {
            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }
            auth.currentUser!!.updateProfile(profileUpdates)
                .addOnCompleteListener { update_task ->
                    if (update_task.isSuccessful) {
                        onSuccess(
                            User(
                                id = auth.currentUser?.uid ?: "",
                                email = auth.currentUser?.email ?: "",
                                name = name,
                                profileImage = auth.currentUser?.photoUrl.toString()
                            )
                        )
                    }
                }
        }
    }

    fun register(
        onSuccess: (result: User) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val profileUpdates = userProfileChangeRequest {
                        displayName = name
                    }
                    auth.currentUser!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { update_task ->
                            if (update_task.isSuccessful) {
                                onSuccess(
                                    User(
                                        id = auth.currentUser?.uid ?: "", 
                                        email = auth.currentUser?.email ?: "", 
                                        name = auth.currentUser?.displayName ?: "",
                                        profileImage = auth.currentUser?.photoUrl.toString()
                                    )
                                )
                            }
                        }
                } else {
                    onFailure(task.exception ?: Exception("Unknown exception."))
                }
            }
    }

    fun login(
        onSuccess: (result: User) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(
                        User(
                            id = auth.currentUser?.uid ?: "",
                            email = auth.currentUser?.email ?: "",
                            name = auth.currentUser?.displayName ?: "",
                            profileImage = auth.currentUser?.photoUrl.toString()
                        )
                    )
                } else {
                    onFailure(task.exception ?: Exception("Unknown exception."))
                }
            }
    }

    fun isUserLoggedIn(
        onSuccess: (isLoggedIn: Boolean) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val currentUser = auth.currentUser
        onSuccess(currentUser != null)
    }

    fun recoverPassword(onSuccess: () -> Unit, onFailure: (exception: Exception) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception ?: Exception("Unknown exception."))
                }
            }
    }

    fun getUser(
        onSuccess: (result: User) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            onSuccess(
                User(
                    id = currentUser.uid,
                    email = currentUser.email ?: "",
                    name = currentUser.displayName ?: "",
                    profileImage = currentUser.photoUrl.toString()
                )
            )
        } else {
            onFailure(Exception("User is not logged in."))
        }
    }

    fun logout(onSuccess: () -> Unit, onFailure: (exception: Exception) -> Unit) {
        try {
            auth.signOut()
            onSuccess()
        } catch (e: Exception) {
            onFailure(e)
        }
    }
}