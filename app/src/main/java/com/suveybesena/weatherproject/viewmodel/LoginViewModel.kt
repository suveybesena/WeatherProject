package com.suveybesena.weatherproject.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.room.Fts4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.suveybesena.weatherproject.R
import com.suveybesena.weatherproject.view.LoginRegisterFragmentDirections
import java.util.*

class LoginViewModel :ViewModel( ) {

    private lateinit var storage : FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore

    fun pickPhoto(mail: String, password:String, view:View, context:Context){

        storage= FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()


        auth.signInWithEmailAndPassword(mail, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                var currentUser = auth.currentUser?.email.toString()
                context?.let {
                    Toast.makeText(it, "Welcome, ${currentUser}", Toast.LENGTH_LONG).show()
                }

                var action =
                    LoginRegisterFragmentDirections.actionLoginRegisterFragmentToMainFragment()
                Navigation.findNavController(view).navigate(action)

            }

        }.addOnFailureListener { exception ->
            context?.let {
                Toast.makeText(it, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun onc_SignUp(mail: String, password: String, context: Context, view: View,pickedImage:Uri) {
        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener { task ->
            // asenkron çalışır.
            if (task.isSuccessful) {
                context?.let {
                    Toast.makeText(it, "Welcome, ${mail}", Toast.LENGTH_LONG).show()
                }

                var action =
                    LoginRegisterFragmentDirections.actionLoginRegisterFragmentToMainFragment()
                Navigation.findNavController(view).navigate(action)

            }
        }.addOnFailureListener { exception ->
            context?.let {
                Toast.makeText(it, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }
        val uuid = UUID.randomUUID()
        var imageName = "${uuid}.jpeg"
        val reference = storage.reference
        var imageReference = reference.child("images").child(imageName)
        if (pickedImage != null) {
            imageReference.putFile(pickedImage!!).addOnSuccessListener { taskSnapshot ->
                val uploadedImageReference =
                    FirebaseStorage.getInstance().reference.child("images").child(imageName)
                uploadedImageReference.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    val currentUserMail = auth.currentUser!!.email.toString()
                    val date = com.google.firebase.Timestamp.now()

                    val postHashmap = hashMapOf<String, Any>()
                    postHashmap.put("downloadUri", imageUrl)
                    postHashmap.put("currentUserMail", currentUserMail)
                    postHashmap.put("date", date)

                    database.collection("Users").add(postHashmap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                        }
                    }.addOnFailureListener { exception ->
                        context?.let {

                            Toast.makeText(
                                it,
                                exception.localizedMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }

                }

            }.addOnFailureListener { exception ->
                context?.let {
                    Toast.makeText(it, exception.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }

            }


        }

    }



}