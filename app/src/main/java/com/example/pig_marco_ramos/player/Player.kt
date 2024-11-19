package com.example.pig_marco_ramos.player

import android.graphics.Color
import android.widget.TextView

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