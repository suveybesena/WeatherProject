package com.suveybesena.weatherproject.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.type.TimeOfDay
import com.suveybesena.weatherproject.R
import com.suveybesena.weatherproject.adapter.RecyclerAdapter
import com.suveybesena.weatherproject.viewmodel.MainViewModel
import com.suveybesena.weatherproject.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_notes.*
import java.time.DayOfWeek
import java.time.YearMonth
import java.util.*
import kotlin.collections.ArrayList

class NotesFragment : Fragment() {
    private lateinit var viewModel: NoteViewModel
    private lateinit var auth : FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database :FirebaseFirestore
    var noteList = ArrayList<String>()
    private lateinit var adapter: RecyclerAdapter


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


        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database =FirebaseFirestore.getInstance()



        viewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)



        btw_AddNotes.setOnClickListener {
            onc_AddNotes(view)
        }

        var layoutManager = LinearLayoutManager(requireContext())
        recycler_view.layoutManager = layoutManager
        adapter = RecyclerAdapter(noteList)
        recycler_view.adapter = adapter

        readUserData()
        readNotesData()

        imw_NoteProfile.setOnClickListener {
            var action = NotesFragmentDirections.actionNotesFragmentToLoginRegisterFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }


    fun onc_AddNotes(view: View){


        val notes = edt_Task.text.toString()
        val date = com.google.firebase.Timestamp.now()

        val postHashmap = hashMapOf<String, Any>()
        postHashmap.put("notes", notes)
        postHashmap.put("date", date)

        database.collection("Notes").add(postHashmap).addOnCompleteListener { task ->
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

    fun readUserData(){
        var currentUser = auth.currentUser?.email.toString()
        database.collection("Users").whereEqualTo("currentUserMail",currentUser )
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null){
                    context?.let {
                        Toast.makeText(it, exception.localizedMessage,Toast.LENGTH_LONG).show()
                    }


                }else{
                    if (snapshot != null){
                        if (!snapshot.isEmpty){

                            var documentList =snapshot.documents

                            //postList.clear()

                            for (document in documentList){

                                val user = document.get("currentUserMail") as String
                                val downloadUri = document.get("downloadUri") as String
                                Glide.with(this).load(downloadUri)
                                    .into(imw_NoteProfile)
                                tw_noteViewDesc.text= currentUser



                            }

                        }
                    }
                }
            }

    }

    fun readNotesData(){
        database.collection("Notes")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                if (exception != null){
                    context?.let {
                        Toast.makeText(it, exception.localizedMessage,Toast.LENGTH_LONG).show()
                    }


                }else{
                    if (snapshot != null){
                        if (!snapshot.isEmpty){

                            var documentList =snapshot.documents

                            noteList.clear()

                            for (document in documentList){

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