package com.example.pig_marco_ramos

import android.widget.TextView

class Player(var name: String, var label: TextView, var currentPointsCounter: TextView, var totalPointsCounter: TextView) {

    var totalPoints: Int = 0

    var currentPoints: Int = 0
}