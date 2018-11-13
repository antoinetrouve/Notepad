package com.antoinetrouve.notepad

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class NoteAdpater (val notes: List<Note>, val itemClickListener: View.OnClickListener)
    : RecyclerView.Adapter<NoteAdpater.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.card_view)
        val titleView = itemView.findViewById<TextView>(R.id.title)
        val excerptView = itemView.findViewById<TextView>(R.id.excerpt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.cardView.setOnClickListener(itemClickListener)
        holder.cardView.tag = position
        holder.titleView.text = note.title
        holder.excerptView.text = note.text
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}
