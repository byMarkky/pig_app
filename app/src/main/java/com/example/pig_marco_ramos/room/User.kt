package com.example.pig_marco_ramos.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "birthDate") val birthDate: String,
    @ColumnInfo(name = "image") val image: String?
): Serializable {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
        get() = field
        set(value) {
            field = value
        }
}