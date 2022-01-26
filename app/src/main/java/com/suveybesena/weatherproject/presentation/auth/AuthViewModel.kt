package com.suveybesena.weatherproject.presentation.auth

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AuthViewModel : ViewModel() {

    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    fun pickPhoto(mail: String, password: String, view: View, context: Context) {

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()


        auth.signInWithEmailAndPassword(mail, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val currentUser = auth.currentUser?.email.toString()

                Toast.makeText(context, "Welcome, ${currentUser}", Toast.LENGTH_LONG).show()


                val action =
                    AuthFragmentDirections.actionLoginRegisterFragmentToMainFragment()
                Navigation.findNavController(view).navigate(action)

            }

        }.addOnFailureListener { exception ->
            Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_LONG).show()

        }
    }

    fun onclickSignUp (mail: String, password: String, context: Context, view: View, pickedImage: Uri) {
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener { task ->
            // asenkron çalışır.
            if (task.isSuccessful) {
                Toast.makeText(context, "Welcome, ${mail}", Toast.LENGTH_LONG).show()
                var action =
                    AuthFragmentDirections.actionLoginRegisterFragmentToMainFragment()
                Navigation.findNavController(view).navigate(action)

            }
        }.addOnFailureListener { exception ->
            Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_LONG).show()

        }
        val uuid = UUID.randomUUID()
        var imageName = "${uuid}.jpeg"
        val reference = storage.reference
        var imageReference = reference.child("images").child(imageName)
        if (pickedImage != null) {
            imageReference.putFile(pickedImage).addOnSuccessListener { taskSnapshot ->
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
                        Toast.makeText(
                            context,
                            exception.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()

                    }

                }

            }.addOnFailureListener { exception ->
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()


            }


        }

    }


}