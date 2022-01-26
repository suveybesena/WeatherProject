package com.suveybesena.weatherproject.presentation.auth

import android.app.Activity
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.suveybesena.weatherproject.R
import kotlinx.android.synthetic.main.fragment_login_register.*


class AuthFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var contentResolver: ContentResolver
    private lateinit var auth: FirebaseAuth
    var pickedImage: Uri? = null
    var bitmap: Bitmap? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore

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

        getInstance()
        checkCurrentUser()
        initListeners()

    }

    private fun initListeners() {
        edt_SignUp.setOnClickListener { view ->
            onclickSignUp(view)
        }
        btw_Login.setOnClickListener { view ->
            onclickLogin(view)
        }
        imw_Account.setOnClickListener {
            onclickAccountImage()
        }
    }

    private fun checkCurrentUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val action = AuthFragmentDirections.actionLoginRegisterFragmentToMainFragment()
            Navigation.findNavController(requireView()).navigate(action)

        }
    }

    private fun getInstance() {
        contentResolver = requireActivity().contentResolver
        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    private fun onclickSignUp(view: View) {
        val mail = edt_Mail.text.toString()
        val password = edt_Password.text.toString()
        viewModel.onclickSignUp(mail, password, requireContext(), view, pickedImage!!)

    }

    private fun onclickLogin(view: View) {
        val mail = edt_Mail.text.toString()
        val password = edt_Password.text.toString()
        viewModel.pickPhoto(mail, password, view, requireContext())
    }

    private fun onclickAccountImage() {
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
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

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