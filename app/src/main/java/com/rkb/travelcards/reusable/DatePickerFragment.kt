package com.rkb.travelcards.reusable

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user

        val data = bundleOf(
            "year" to year, "month" to month+1, "day" to day
        ) // なぜか, monthは本来の月より1低い値が渡される。

        // FragmentManager経由で結果を伝える
        parentFragmentManager.setFragmentResult("keyDate", data)
    }
}