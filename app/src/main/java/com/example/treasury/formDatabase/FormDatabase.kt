package com.example.treasury.formDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Form::class], version = 1, exportSchema = false)
abstract class FormDatabase : RoomDatabase(){
    abstract fun getDao(): FormDao

    companion object{
        private var instance: FormDatabase? = null
        private val databaseName = "Treasury.db"

        fun getInstance(context: Context): FormDatabase {
            instance ?: synchronized(FormDatabase::class){
                instance = Room.databaseBuilder(context, FormDatabase::class.java, databaseName)
                    .build()
            }
            return instance!!
        }
    }
}