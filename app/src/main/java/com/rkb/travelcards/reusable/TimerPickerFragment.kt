package com.rkb.travelcards.reusable

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.rkb.travelcards.R

import androidx.core.os.bundleOf

class TimerPickerFragment : DialogFragment() {

    lateinit var npHour : NumberPicker
    lateinit var npMinute : NumberPicker

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            // 事前準備: viewを用意
            val view = View.inflate(activity, R.layout.fragment_timer_picker, null)

            npHour = view.findViewById<NumberPicker>(R.id.numberPicker_hour)
            npHour.minValue = 0
            npHour.maxValue = 23
            npHour.value = 1

            npMinute = view.findViewById<NumberPicker>(R.id.numberPicker_minute)
            npMinute.minValue = 0
            npMinute.maxValue = 59
            npMinute.value = 0
            npMinute.setOnValueChangedListener{ picker, oldVal, newVal ->
//                Log.v("", "$oldVal, $newVal")
                if (oldVal == picker.minValue && newVal == picker.maxValue) npHour.value -= 1
                if (oldVal == picker.maxValue && newVal == picker.minValue) npHour.value += 1
                if (npHour.value == 0 && newVal == 0) picker.value = 1
            }

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
                .setView(view)

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun submit(inputText: String?) {
        val data = bundleOf(
            "hour" to npHour.value,
            "minute" to npMinute.value
        )

        // FragmentManager経由で結果を伝える
        parentFragmentManager.setFragmentResult("setTimer", data)
    }
}

