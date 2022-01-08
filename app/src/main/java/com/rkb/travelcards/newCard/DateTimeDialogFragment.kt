package com.rkb.travelcards.newCard

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.rkb.travelcards.R

import android.content.Intent
import java.util.*


class DateTimeDialogFragment : DialogFragment() {

    lateinit var startDateTime : Calendar

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ad : AlertDialog
        ad = activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("日付・時刻を指定しましょう")
                .setPositiveButton("決定",
                    DialogInterface.OnClickListener { dialog, id ->
                        submit(startDateTime)
                    })
                .setNegativeButton("キャンセル",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
                .setView(R.layout.fragment_date_time_dialog)

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")


        return ad
    }

    fun submit(inputCalendar: Calendar?) {
//        val target = targetFragment ?: return
//        val intent = Intent()
//        intent.putExtra("startDateTime", inputCalendar)
//        target.onActivityResult(targetRequestCode, Activity.RESULT_OK, data)



    }

    fun setStartDate(year: Int, month: Int, day: Int) {
        startDateTime.set(year, month, day)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            if (resultCode == DialogInterface.BUTTON_POSITIVE) {
                // positive_button 押下時の処理
            } else if (resultCode == DialogInterface.BUTTON_NEGATIVE) {
                // negative_button 押下時の処理
            }
        }
    }
}