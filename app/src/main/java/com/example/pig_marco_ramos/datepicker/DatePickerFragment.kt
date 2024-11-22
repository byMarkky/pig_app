package com.example.pig_marco_ramos.datepicker

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment

class DatePickerFragment(
    val listener: (day: Int, month: Int, year: Int) -> Unit
) : DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        // P1: YEAR, P2: MONTH, P3: DAY
        listener(p3, p2, p1)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val picker = DatePickerDialog(activity as Context, this, year, month, day)

        return picker
    }


}