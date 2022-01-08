package com.rkb.travelcards.reusable

import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.rkb.travelcards.R
import android.view.View

import android.widget.TextView
import android.widget.Toast

class TimerPickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 0時間0分がデフォルト
        val hour = 0
        val minute = 0

        // Create a new instance of TimePickerDialog and return it
//        return TimePickerDialog(activity, android.R.style.Theme_Dialog, this, hour, minute, DateFormat.is24HourFormat(activity))
//        return TimePickerDialog(activity, R.style.SpinnerTimePicker, this, hour, minute, DateFormat.is24HourFormat(activity))
//        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))

        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("所要時間を指定しましょう")
                .setPositiveButton("決定",
                    DialogInterface.OnClickListener { dialog, id ->
                        // FIRE ZE MISSILES!
                        submit("answer")
                    })
                .setNegativeButton("キャンセル",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
                .setView(R.layout.fragment_time_picker_with_spinner)

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // Do something with the time chosen by the user
    }

    fun submit(inputText: String?) {
        val target = targetFragment ?: return
        val data = Intent()
        data.putExtra(Intent.EXTRA_TEXT, inputText)
        target.onActivityResult(targetRequestCode, Activity.RESULT_OK, data)
    }
}