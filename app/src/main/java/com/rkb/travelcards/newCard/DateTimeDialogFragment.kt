package com.rkb.travelcards.newCard

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.rkb.travelcards.R
import android.app.Activity

import android.content.Intent
import androidx.fragment.app.Fragment


class DateTimeDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("日付・時刻を指定しましょう")
                .setPositiveButton("決定",
                    DialogInterface.OnClickListener { dialog, id ->
                        // FIRE ZE MISSILES!
                        submit("answer")
                    })
                .setNegativeButton("キャンセル",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun submit(inputText: String?) {
        val target = targetFragment ?: return
        val data = Intent()
        data.putExtra(Intent.EXTRA_TEXT, inputText)
        target.onActivityResult(targetRequestCode, Activity.RESULT_OK, data)
    }
}