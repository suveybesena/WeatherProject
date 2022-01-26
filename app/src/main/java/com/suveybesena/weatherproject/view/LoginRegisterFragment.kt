package com.suveybesena.weatherproject.view

import android.app.Activity
import android.content.ContentProvider
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.suveybesena.weatherproject.R
import com.suveybesena.weatherproject.viewmodel.LoginViewModel
import com.suveybesena.weatherproject.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_login_register.*
import java.util.*


class LoginRegisterFragment : Fragment() {


    private lateinit var  contentResolver: ContentResolver

    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: LoginViewModel

    var pickedImage : Uri? = null
    var bitmap : Bitmap? = null

    private lateinit var storage : FirebaseStorage

    private lateinit var database : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contentResolver = requireActivity().contentResolver
        storage= FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)


        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null){
            var action = LoginRegisterFragmentDirections.actionLoginRegisterFragmentToMainFragment()
            Navigation.findNavController(view).navigate(action)

        }

        edt_SignUp.setOnClickListener {
            onc_SignUp(it)
        }
        btw_Login.setOnClickListener {
            onc_Login(it)
        }

        imw_Account.setOnClickListener {
            onc_AccountImage(it)
        }

    }

    fun onc_SignUp(view: View) {
        var mail = edt_Mail.text.toString()
        var password = edt_Password.text.toString()
    viewModel.onc_SignUp(mail,password,requireContext(), view,pickedImage!!)

    }

    fun onc_Login(view: View) {
        var mail = edt_Mail.text.toString()
        var password = edt_Password.text.toString()
        viewModel.pickPhoto(mail, password,view,requireContext())

    }





    fun onc_AccountImage (view: View) {

        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )

        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode ==2 && resultCode== Activity.RESULT_OK && data != null) {

            pickedImage = data.data

            if (pickedImage != null) {

                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(this.contentResolver, pickedImage!!)
                    bitmap = ImageDecoder.decodeBitmap(source)
                    imw_Account.setImageBitmap(bitmap)

                } else {
                    bitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, pickedImage)
                    imw_Account.setImageBitmap(bitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}