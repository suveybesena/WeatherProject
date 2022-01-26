package com.suveybesena.weatherproject.presentation.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.suveybesena.weatherproject.R
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlin.collections.ArrayList

class NotesFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore
    private lateinit var adapter: NotesAdapter
    var noteList = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getInstance()
        onClickListeners()
        layoutAdapter()
        readUserData()
        readNotesData()

    }

    private fun getInstance() {
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()
    }

    private fun layoutAdapter() {
        var layoutManager = LinearLayoutManager(requireContext())
        recycler_view.layoutManager = layoutManager
        adapter = NotesAdapter(noteList)
        recycler_view.adapter = adapter
    }

    private fun onClickListeners() {
        btw_AddNotes.setOnClickListener {
            addNotes()
        }
        imw_NoteProfile.setOnClickListener {
            val action = NotesFragmentDirections.actionNotesFragmentToLoginRegisterFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
    }


    private fun addNotes() {

        val notes = edt_Task.text.toString()
        val date = com.google.firebase.Timestamp.now()
        val email = auth.currentUser?.email.toString()

        val postHashmap = hashMapOf<String, Any>()
        postHashmap.put("notes", notes)
        postHashmap.put("date", date)
        postHashmap.put("email", email)

        database.collection("Notes").add(postHashmap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                readNotesData()
            }
        }.addOnFailureListener { exception ->
            context?.let {

               /*Toast.makeText(
                    it,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()*/
            }

        }

    }

    private fun readUserData() {
        var currentUser = auth.currentUser?.email.toString()
        database.collection("Users").whereEqualTo("currentUserMail", currentUser)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    context?.let {
                        //Toast.makeText(it, exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }


                } else {
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {

                            var documentList = snapshot.documents
                            //postList.clear()
                            for (document in documentList) {
                                val user = document.get("currentUserMail") as String
                                val downloadUri = document.get("downloadUri") as String
                                Glide.with(this).load(downloadUri)
                                    .into(imw_NoteProfile)
                                tw_noteViewDesc.text = currentUser

                            }

                        }
                    }
                }
            }

    }

    private fun readNotesData() {
        var currentUser = auth.currentUser?.email.toString()
        database.collection("Notes").whereEqualTo("email", currentUser)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                if (exception != null) {
                    context?.let {
                       // Toast.makeText(it, exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }

                } else {
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {
                            var documentList = snapshot.documents
                            noteList.clear()
                            for (document in documentList) {
                                val notes = document.get("notes") as String

                                noteList.add(notes)
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
    }
}