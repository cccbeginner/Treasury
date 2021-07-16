package com.example.treasury.formDatabase

import androidx.room.*

@Dao
interface FormDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun justInsert(form: Form)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(form: Form)

    @Delete
    suspend fun delete(form: Form)

    @Query("SELECT * FROM tbl_form WHERE year_month = :yearMonth and type = :type ORDER BY _id ASC")
    suspend fun getByType(yearMonth: Int, type: Int): Array<Form>

    @Query("SELECT * FROM tbl_form WHERE year_month = :yearMonth and type = :type and name = :name")
    fun getTheForm(yearMonth: Int, type: Int, name: String): Form?

    @Transaction
    suspend fun insert(form: Form) : Boolean{
        val exist = getTheForm(form.yearMonth, form.type, form.name)
        return if (exist != null){
            false
        }else{
            justInsert(form)
            true
        }
    }
}