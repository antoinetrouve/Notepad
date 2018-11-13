package com.antoinetrouve.notepad.utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.antoinetrouve.notepad.Note
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*

private val TAG = "storage"

fun persistNote(context: Context, note: Note) {

    if (TextUtils.isEmpty(note.filename)) {
        note.filename = UUID.randomUUID().toString() + ".note"
    }

    // Open file from application in private mode (do not authorize another app to use our note
    val fileOutput = context.openFileOutput(note.filename, Context.MODE_PRIVATE)
    val outputStream = ObjectOutputStream(fileOutput)

    // possible to do this, see implementation Serializable in class Note
    outputStream.writeObject(note)
    outputStream.close()
}

fun loadNotes(context: Context): MutableList<Note> {
    val notes = mutableListOf<Note>()

    val notesDir = context.filesDir
    for (filename in notesDir.list()) {
        val note = loadNote(context, filename)
        Log.i(TAG, "Loaded note $note")
        notes.add(note)
    }

    return notes
}

private fun loadNote(context: Context, filename: String): Note {
    val fileinput = context.openFileInput(filename)
    val inputStream = ObjectInputStream(fileinput)
    val note = inputStream.readObject() as Note
    inputStream.close()

    return note
}

fun deleteNote(context: Context, note: Note) {
    context.deleteFile(note.filename)
}