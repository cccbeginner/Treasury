package com.example.treasury.formDatabase

import kotlinx.coroutines.flow.MutableStateFlow

class FormRepository (private val formDao: FormDao) {

    /*
     * Here, I store in lists even there are
     *  some type with only one data.
     *
     * mutable map <yearMonth, <type, data> >
     */
    val listFlowMap = mutableMapOf<Int, MutableStateFlow< ArrayList< Form > > >()

    init {
        /*
         * Fill empty stuff to these maps.
         */
        for(type in Form.dataTypeArray){
            listFlowMap[type] = MutableStateFlow(ArrayList())
        }
    }

    suspend fun fetchData(yearMonth: Int){
        for (type in Form.dataTypeArray) {
            val forms = formDao.getByType(yearMonth, type)
            val formArray = forms.toCollection(ArrayList())
            listFlowMap[type]?.emit(formArray)
        }
    }

    suspend fun insertMany(formArrayList: ArrayList<Form>){
        for (k in formArrayList) {
            k.id = null
        }
        formDao.insertMany(formArrayList.toTypedArray())
    }
    suspend fun insert(form: Form){
        formDao.insert(form)
    }
    suspend fun deleteByCurrentYearMonth(yearMonth: Int){
        formDao.deleteByYearMonth(yearMonth)
    }
}