package com.rkb.travelcards.newCard

import android.annotation.SuppressLint
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
import androidx.fragment.app.setFragmentResultListener
import com.rkb.travelcards.reusable.DatePickerFragment
import com.rkb.travelcards.reusable.TimePickerFragment
import java.util.*


class DateTimeDialogFragment : DialogFragment() {

    lateinit var startDateTime : Calendar

    @SuppressLint("ResourceAsColor")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ad : AlertDialog

        return activity?.let {

            // 事前準備: viewを用意
            val view = View.inflate(activity, R.layout.fragment_date_time_dialog, null)

            val etDate = view.findViewById<EditText>(R.id.editText_Date)
            etDate.setHintTextColor(R.color.red)
            etDate.setOnClickListener {
                Log.v("", "押されてんぞ")
//                showDatePickerDialog(view)
                DatePickerFragment().show(childFragmentManager, "datePicker")
            }
//
//            val etTime = view.findViewById<EditText>(R.id.editText_Time)
//            etTime.setBackgroundColor(R.color.red)
//            etTime.setOnClickListener {
//                Log.v("", "押されてんぞ")
//                showTimePickerDialog(view)
//            }

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
                .setView(view)

            Log.v("", builder.toString())

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun submit(inputCalendar: Calendar?) {
//        val target = targetFragment ?: return
//        val intent = Intent()
//        intent.putExtra("startDateTime", inputCalendar)
//        target.onActivityResult(targetRequestCode, Activity.RESULT_OK, data)

    }

    fun showDatePickerDialog(v: View) {
//        val dDate = Bundle()
//
//        val dfDate = DatePickerFragment()
//        Log.v("", "どもー、シャムでっす")
//        dfDate.setTargetFragment(this, 100)
//        Log.v("", "さて、今日はオフ会当日ですけども")
//        dfDate.show(childFragmentManager, "my_dialog1");
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("", "はろー")
        childFragmentManager.setFragmentResultListener("input", this) { key, data ->
            val text = data.getString("text", "")
            // 結果を使った処理
            Log.v("", "Good: $text")
        }
    }

    fun showTimePickerDialog(v: View) {
//        TimePickerFragment().show(supportFragmentManager, "timePicker")
        val dfTime = TimePickerFragment()
        dfTime.setTargetFragment(this, 123)
        dfTime.show(childFragmentManager, "my_dialog2");
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