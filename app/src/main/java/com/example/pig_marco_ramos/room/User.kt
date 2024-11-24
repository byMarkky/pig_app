package com.example.pig_marco_ramos.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "birthDate") val birthDate: String
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
        get() = field
        set(value) {
            field = value
        }
}