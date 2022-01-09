package com.rkb.travelcards.newCard

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.rkb.travelcards.R

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.rkb.travelcards.reusable.DatePickerFragment
import com.rkb.travelcards.reusable.TimePickerFragment
import java.util.*

class DateTimeDialogFragment : DialogFragment() {

    lateinit var startDateTime : Calendar
    val dfDate = DatePickerFragment()
    val dfTime = TimePickerFragment()

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

            Log.v("", builder.toString())

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

        Log.v("", "ほげほげほげー！")

        val etDate = ad.findViewById<EditText>(R.id.editText_Time)
        Log.v("", etDate.toString())
        etDate?.setOnClickListener {
            Log.v("", "押されてんぞ")
//            showDatePickerDialog((view)ad)
        }

        return ad
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun submit(inputCalendar: Calendar?) {
//        val target = targetFragment ?: return
//        val intent = Intent()
//        intent.putExtra("startDateTime", inputCalendar)
//        target.onActivityResult(targetRequestCode, Activity.RESULT_OK, data)

    }

    fun showDatePickerDialog(v: View?) {
//        DatePickerFragment().show(supportFragmentManager, "datePicker")
        dfDate.setTargetFragment(this, 100)
        dfDate.show(childFragmentManager, "my_dialog");
    }

    fun showTimePickerDialog(v: View?) {
//        TimePickerFragment().show(supportFragmentManager, "timePicker")
        dfTime.setTargetFragment(this, 123)
        dfTime.show(childFragmentManager, "my_dialog");
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