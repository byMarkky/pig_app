package com.example.pig_marco_ramos

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.widget.TextView
import androidx.annotation.RequiresApi

class Player(
    var name: String?,
    var label: TextView?,
    var currentPointsCounter: TextView?,
    var totalPointsCounter: TextView?,
    var _disable: Boolean) {

    init {
        label?.text = name ?: ""
    }

    var totalPoints: Int = 0

    var currentPoints: Int = 0

    var disable: Boolean = _disable
        set(value) {
            if (!value) {
                label?.setTextColor(Color.WHITE)
            } else {
                label?.setTextColor(Color.GRAY)
            }
            field = value
        }



}