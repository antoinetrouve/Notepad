package com.antoinetrouve.notepad

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

class NoteDetailActivity : AppCompatActivity() {

    companion object {
        /** The return code for edit note action */
        val REQUEST_EDIT_NOTE = 1

        /** Bundle extra key for Note*/
        val EXTRA_NOTE = "note"

        /** Bundle extra key for note index */
        val EXTRA_NOTE_INDEX = "noteIndex"

        /** The key for Save action */
        val ACTION_SAVE_NOTE = "com.antoinetrouve.notepad.actions.ACTION_SAVE_NOTE"

        /** The key for Delete Action*/
        val ACTION_DELETE_NOTE = "com.antoinetrouve.notepad.actions.ACTION_DELETE_NOTE"
    }

    lateinit var note: Note
    var noteIndex: Int = -1

    lateinit var titleView: TextView
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        // Set Toolbar menu
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        //display back arrow
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Get the Note to show
        note = intent.getParcelableExtra<Note>(EXTRA_NOTE)
        noteIndex = intent.getIntExtra(EXTRA_NOTE_INDEX, -1)

        // Get element view
        titleView = findViewById<TextView>(R.id.title)
        textView = findViewById<TextView>(R.id.text)

        // Set element view with the note
        titleView.text = note.title
        textView.text = note.text
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_note_detail, menu)
        return true
    }

    /**
     * Manage the click event on toolbar menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                // Save Note
                saveNote()
                return true
            }
            R.id.action_delete -> {
                // Delete Note
                showConfirmDeleteNoteDialog()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /**
     * Display the confirm dialog to validate the delete action
     */
    private fun showConfirmDeleteNoteDialog() {
        val confirmFragment = ConfirmDeleteNoteDialogFragment(note.title)
        confirmFragment.listener = object: ConfirmDeleteNoteDialogFragment.ConfirmDeleteDialogListener {
            override fun onDialogPositiveClick() {
                deleteNote()
            }

            override fun onDialogNegativeClick() { }
        }

        confirmFragment.show(supportFragmentManager, "confirmDeleteDialog")
    }

    /**
     * Save a Note
     */
    fun saveNote() {
        note.title = titleView.text.toString()
        note.text = textView.text.toString()

        // Intent to set information
        intent = Intent(ACTION_SAVE_NOTE)
        intent.putExtra(EXTRA_NOTE, note as Parcelable)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)

        // Back to the NoteListActivity with a code result
        setResult(Activity.RESULT_OK, intent)

        // Finish this activity and start NoteListActivity
        finish()
    }

    /**
     * Delete a Note
     */
    fun deleteNote() {
        intent = Intent(ACTION_DELETE_NOTE)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
