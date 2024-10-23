package com.example.pig_marco_ramos

import android.widget.Button
import android.widget.TextView
import androidx.core.view.isGone

class Player(var name: String, var label: TextView, var holdButton: Button, var currentPointsCounter: TextView) {

    var totalPoints: Int = 0

    var currentPoints: Int = 0
}