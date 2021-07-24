package com.example.treasury.formDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Form::class], version = 2, exportSchema = false)
abstract class FormDatabase : RoomDatabase(){
    abstract fun getDao(): FormDao

    companion object{
        private var instance: FormDatabase? = null
        private val databaseName = "Treasury.db"

        fun getInstance(context: Context): FormDatabase {
            instance ?: synchronized(FormDatabase::class){
                instance = Room.databaseBuilder(context, FormDatabase::class.java, databaseName)
                    .addMigrations(MIGRATION)
                    .build()
            }
            return instance!!
        }

        val MIGRATION : Migration = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE tbl_form RENAME TO tbl_form_old")
                database.execSQL("CREATE TABLE tbl_form (" +
                        "_id INTEGER PRIMARY KEY," +
                        "year_month INTEGER NOT NULL," +
                        "type INTEGER NOT NULL," +
                        "name TEXT NOT NULL," +
                        "money REAL NOT NULL," +
                        "note TEXT NOT NULL)")
                database.execSQL("DROP INDEX IF EXISTS index_tbl_form_year_month_type_name")
                database.execSQL("CREATE UNIQUE INDEX index_tbl_form_year_month_type_name \n" +
                        "ON tbl_form(year_month, type, name);")
                database.execSQL("INSERT INTO tbl_form SELECT * FROM tbl_form_old")
                database.execSQL("DROP TABLE tbl_form_old")
            }
        }
    }
}