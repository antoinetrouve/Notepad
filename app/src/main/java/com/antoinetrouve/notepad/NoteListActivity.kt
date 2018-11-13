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

        // Set toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set Create Note button
        findViewById<FloatingActionButton>(R.id.create_note_fab).setOnClickListener(this);

        // Load the Notes if already exist
        notes = loadNotes(this)

        // Prepare the list item
        adapter = NoteAdpater(notes, this)
        val recyclerView = findViewById<RecyclerView>(R.id.notes_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        coordinatorLayout = findViewById<CoordinatorLayout>(R.id.coordinator_layout)

    }

    /**
     * Manage the result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // If no data or not result code 'ok' , do nothing
        if ( resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        // Process the edit note
        when (requestCode) {
            NoteDetailActivity.REQUEST_EDIT_NOTE -> processEditNoteResult(data)
        }

    }

    /**
     * Manage the delete or save Note Action
     */
    private fun processEditNoteResult(data: Intent) {
        val noteIndex = data.getIntExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, -1)

        when (data.action) {
            NoteDetailActivity.ACTION_SAVE_NOTE -> {
                // Get the note newly created and save it
                val note = data.getParcelableExtra<Note>(NoteDetailActivity.EXTRA_NOTE)
                saveNote(note, noteIndex)
            }
            NoteDetailActivity.ACTION_DELETE_NOTE -> {
                // Delete the note
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

    /**
     * Start the new Activity to create the note
     */
    private fun createNewNote() {
        showNoteDetail(-1)
    }

    /**
     * Delete the note
     * @var Int noteIndex the note Index to delete
     */
    private fun deleteNote(noteIndex: Int) {
        // if note doesn't exist, do nothing
        if (noteIndex < 0) {
            return
        }
        val note = notes.removeAt(noteIndex)
        // delete the note file
        deleteNote(this, note)
        // Update the view
        adapter.notifyDataSetChanged()

        // Inform the user to the note has been deleted with success
        Snackbar.make(coordinatorLayout, "${note.title} supprimé", Snackbar.LENGTH_SHORT)
            .show()
    }

    /**
     * Save a note
     * @var Note note The note to save
     * @var Int noteIndex The note index
     */
    fun saveNote(note: Note, noteIndex: Int) {
        // Save the note into phone
        persistNote(this, note)
        if (noteIndex < 0) {
            // add the note to the list if it's a new note
            notes.add(0, note)
        } else {
            // Update data with the new note
            notes[noteIndex] = note
        }

        // Notify adapter that the data has changed
        adapter.notifyDataSetChanged()
    }

    /**
     * Show the note Detail in a new Activity
     * @var Int noteIndex The note index to show
     */
    fun showNoteDetail(noteIndex: Int) {
        // Check if it's a new note
        val note = if (noteIndex < 0) Note() else notes[noteIndex]

        // Prepare the data to send to the new Activity
        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE, note as Parcelable)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, noteIndex)

        // Start activity but specify, we waiting a response to manage it
        startActivityForResult(intent, NoteDetailActivity.REQUEST_EDIT_NOTE)
    }
}
