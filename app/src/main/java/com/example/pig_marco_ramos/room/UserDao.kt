package com.example.pig_marco_ramos.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM user u WHERE u.name = :name")
    fun getUser(name: String): User

    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE TRIM(name) = TRIM(:name) AND TRIM(password) = TRIM(:password) LIMIT 1)")
    fun exist(name: String, password: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE LOWER(TRIM(name)) = LOWER(TRIM(:name)) LIMIT 1)")
    fun existsName(name: String): Boolean

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("DELETE FROM user")
    fun clean()

}