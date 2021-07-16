package com.example.treasury.formDatabase

import androidx.room.*

@Dao
interface FormDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(formArray : Array<Form>)

    @Query("SELECT * FROM tbl_form WHERE year_month = :yearMonth and type = :type ORDER BY _id ASC")
    suspend fun getByType(yearMonth: Int, type: Int): Array<Form>

    @Query("DELETE FROM tbl_form WHERE year_month = :yearMonth")
    suspend fun deleteByYearMonth(yearMonth: Int)
}