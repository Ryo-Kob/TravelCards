package com.rkb.travelcards.newCard

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.rkb.travelcards.R

import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import com.rkb.travelcards.reusable.DatePickerFragment
import com.rkb.travelcards.reusable.TimePickerFragment
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class DateTimeDialogFragment : DialogFragment() {

    lateinit var startDate : LocalDate
    lateinit var startTime : LocalTime
    lateinit var etDate : EditText
    lateinit var etTime : EditText
    var isSetDate = false
    var isSetTime = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ad : AlertDialog

        return activity?.let {

            // 事前準備: viewを用意
            val view = View.inflate(activity, R.layout.fragment_date_time_dialog, null)

            etDate = view.findViewById<EditText>(R.id.editText_Date)
            etDate.setOnClickListener {
                DatePickerFragment().show(childFragmentManager, "datePicker")
            }

            etTime = view.findViewById<EditText>(R.id.editText_Time)
            etTime.setOnClickListener {
                TimePickerFragment().show(childFragmentManager, "timePicker")
            }

            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("日付・時刻を指定しましょう")
                .setPositiveButton("決定",
                    DialogInterface.OnClickListener { dialog, id ->
                        submit()
                    })
                .setNegativeButton("キャンセル",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
                .setView(view)

            Log.v("", builder.toString())

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun submit() {
//        val target = targetFragment ?: return
//        val intent = Intent()
//        intent.putExtra("startDateTime", inputCalendar)
//        target.onActivityResult(targetRequestCode, Activity.RESULT_OK, data)

        val data = bundleOf(
            "startDate" to startDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
            "startTime" to startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            "isSetDate" to isSetDate,
            "isSetTime" to isSetTime
        )
        parentFragmentManager.setFragmentResult("setStartDateTime", data)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        childFragmentManager.setFragmentResultListener("keyDate", this) { key, data ->
            val year = data.getInt("year", 0)
            val month = data.getInt("month", 0)
            val day = data.getInt("day", 0)

            // 結果を使った処理
            Log.v("", "Good: $year $month $day")
            setStartDateTime(year, month, day)
        }

        childFragmentManager.setFragmentResultListener("keyTime", this) { key, data ->
            val hourOfDay = data.getInt("hourOfDay", 0)
            val minute = data.getInt("minute", 0)

            // 結果を使った処理
            Log.v("", "Good: $hourOfDay $minute")
            setStartDateTime(hourOfDay, minute)
        }

        startDate = LocalDate.now()
        startTime = LocalTime.now()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setStartDateTime(year: Int, month: Int, day: Int) {
        startDate = LocalDate.of(year, month, day)
        etDate.setText(startDate.format(DateTimeFormatter.ofPattern("M/dd")))
        isSetDate = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setStartDateTime(hourOfDay: Int, minute: Int) {
        startTime = LocalTime.of(hourOfDay, minute)
        etTime.setText(startTime.format(DateTimeFormatter.ofPattern("H:mm")))
        isSetTime = true
    }
}