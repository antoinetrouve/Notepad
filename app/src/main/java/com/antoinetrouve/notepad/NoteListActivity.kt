package com.antoinetrouve.notepad

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import com.antoinetrouve.notepad.utils.deleteNote
import com.antoinetrouve.notepad.utils.loadNotes
import com.antoinetrouve.notepad.utils.persistNote

class NoteListActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var notes : MutableList<Note>
    lateinit var adapter: NoteAdpater
    lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        findViewById<FloatingActionButton>(R.id.create_note_fab).setOnClickListener(this);

        notes = loadNotes(this)
        adapter = NoteAdpater(notes, this)

        val recyclerView = findViewById<RecyclerView>(R.id.notes_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        coordinatorLayout = findViewById<CoordinatorLayout>(R.id.coordinator_layout)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // If no data or not result code ok do nothing
        if ( resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        when (requestCode) {
            NoteDetailActivity.REQUEST_EDIT_NOTE -> processEditNoteResult(data)
        }

    }

    private fun processEditNoteResult(data: Intent) {
        val noteIndex = data.getIntExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, -1)

        when (data.action) {
            NoteDetailActivity.ACTION_SAVE_NOTE -> {
                val note = data.getParcelableExtra<Note>(NoteDetailActivity.EXTRA_NOTE)
                saveNote(note, noteIndex)
            }
            NoteDetailActivity.ACTION_DELETE_NOTE -> {
                deleteNote(noteIndex)
            }
        }
    }

    override fun onClick(view: View) {
        if (view.tag != null) {
            showNoteDetail(view.tag as Int)
        } else {
            when (view.id) {
                R.id.create_note_fab -> createNewNote()
            }
        }
    }

    private fun createNewNote() {
        showNoteDetail(-1)
    }

    private fun deleteNote(noteIndex: Int) {
        // if note doesn't exist, do nothing
        if (noteIndex < 0) {
            return
        }
        val note = notes.removeAt(noteIndex)
        deleteNote(this, note)
        adapter.notifyDataSetChanged()

        Snackbar.make(coordinatorLayout, "${note.title} supprimé", Snackbar.LENGTH_SHORT)
            .show()
    }

    fun saveNote(note: Note, noteIndex: Int) {
        persistNote(this, note)
        if (noteIndex < 0) {
            notes.add(0, note)
        } else {
            // Update data with the new note
            notes[noteIndex] = note
        }

        // Notify adapter that the data has changed
        adapter.notifyDataSetChanged()
    }

    fun showNoteDetail(noteIndex: Int) {
        val note = if (noteIndex < 0) Note() else notes[noteIndex]

        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE, note as Parcelable)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, noteIndex)

        // Commence l'activité en attendant une réponse
        startActivityForResult(intent, NoteDetailActivity.REQUEST_EDIT_NOTE)
    }
}
