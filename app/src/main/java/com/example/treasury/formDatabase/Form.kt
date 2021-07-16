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
    var money: Long,
    var note: String,
) {
    constructor(yearMonth: Int, type: Int, name: String): this(null, yearMonth, type, name, 0, "")
}