package com.example.treasury.formDatabase

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_form", indices = [
    Index(value = ["year_month","type", "name"], unique = true)
])
data class Form(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int?,
    @ColumnInfo(name = "year_month")
    val yearMonth: Int,
    val type: Int,
    val name: String,
    var money: String,
    var note: String,
) {
    constructor(yearMonth: Int, type: Int, name: String): this(null, yearMonth, type, name, "", "")

    companion object{

        // define type value below ...
        const val type_1 = 0
        const val type_1_1 = 1
        const val type_1_2 = 2
        const val type_1_3 = 3
        const val type_2 = 4
        const val type_2_1 = 5
        const val type_2_2 = 6
        const val type_2_3 = 7
        const val type_3 = 8
        const val type_4 = 9
        const val type_5 = 10
        const val type_6 = 11
        const val type_7 = 12
        const val type_ex_rate = 13 // name = {"美股匯率"}
        const val type_year = 14 // name = {"西元年"}
        const val type_month = 15 // name = {"月"}
        const val type_day = 16 // name = {"日"}

        // type array with a list data
        val listTypeArray = arrayOf(type_1_1, type_2_1, type_2_2, type_3, type_4, type_5)

        // type array with one data only
        val singleTypeArray = arrayOf(type_1_2, type_1_3, type_2_3, type_ex_rate)

        // type array with data in database
        val dataTypeArray = arrayOf(type_1_1, type_1_2, type_1_3, type_2_1, type_2_2, type_2_3, type_3, type_4, type_5, type_ex_rate, type_year, type_month, type_day)

        // type array with sum in title
        val sumTypeArray = arrayOf(type_1, type_1_1, type_1_2, type_1_3, type_2, type_2_1, type_2_2, type_2_3, type_3, type_4, type_5, type_6, type_7, type_ex_rate)

        // type array with dates
        val dateTypeArray = arrayOf(type_year, type_month, type_day)

        // type array with all types
        val allTypeArray = arrayOf(type_1, type_1_1, type_1_2, type_1_3, type_2, type_2_1, type_2_2, type_2_3, type_3, type_4, type_5, type_6, type_7, type_ex_rate, type_year, type_month, type_day)
    }
}