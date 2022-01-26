package com.suveybesena.weatherproject.presentation.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suveybesena.weatherproject.R
import kotlinx.android.synthetic.main.recycler_row.view.*

class NotesAdapter (val notesList : ArrayList<String>) :RecyclerView.Adapter<NotesAdapter.NotesVH> (){
    class NotesVH (itemView: View) : RecyclerView.ViewHolder (itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesVH {
        var inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row, parent,false)
        return NotesVH(view)
    }

    override fun onBindViewHolder(holder: NotesVH, position: Int) {
        holder.itemView.recycler_addedNotes.text = notesList.get(position)
    }

    override fun getItemCount(): Int {
        return  notesList.size
    }
}