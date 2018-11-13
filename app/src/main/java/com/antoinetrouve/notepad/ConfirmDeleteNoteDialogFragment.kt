package com.antoinetrouve.notepad

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment

class ConfirmDeleteNoteDialogFragment(val noteTitle: String = "") : DialogFragment() {

    /**
     * Interface to listen on click event
     */
    interface ConfirmDeleteDialogListener {
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    //The interface
    var listener: ConfirmDeleteDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Create the dialog
        val  builder = AlertDialog.Builder(activity)

        // Set message and manage the user response on click event
        builder.setMessage("Êtes-vous sûr de supprimer la note \"$noteTitle\" ?")
            .setPositiveButton("Supprimer",
                DialogInterface.OnClickListener { dialog, id -> listener?.onDialogPositiveClick() })
            .setNegativeButton("Annuler",
                DialogInterface.OnClickListener { dialog, id -> listener?.onDialogNegativeClick() })

        return builder.create()
    }
}