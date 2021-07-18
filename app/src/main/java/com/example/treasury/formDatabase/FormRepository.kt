package com.example.treasury.formDatabase

import com.example.treasury.MyApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.Delegates

class FormRepository (private val formDao: FormDao) {

    // mutable map <yearMonth, <type, data> >
    val listFlowMap = mutableMapOf<Int,  MutableMap< Int, MutableStateFlow< ArrayList< Form > > > >()

    init {
        /*
         * Fill empty stuff to these maps.
         */
        val mapList = mutableMapOf<Int, MutableStateFlow< ArrayList< Form > > >()
        for(type in Form.dataTypeArray){
            mapList[type] = MutableStateFlow(ArrayList())
        }
        for(yearMonth in MyApplication.start..MyApplication.end){
            listFlowMap[yearMonth] = mapList
        }
    }

    suspend fun fetchData(){
        for (type in Form.dataTypeArray) {
            val forms = formDao.getByType(selectedYearMonth, type)
            val formArray = forms.toCollection(ArrayList())
            listFlowMap[selectedYearMonth]?.get(type)?.emit(formArray)
        }
    }

    suspend fun insertMany(formArrayList: ArrayList<Form>){
        formDao.insertMany(formArrayList.toTypedArray())
    }
    suspend fun insert(form: Form){
        formDao.insert(form)
    }
    suspend fun deleteByCurrentYearMonth(){
        formDao.deleteByYearMonth(selectedYearMonth)
    }

    companion object{
        var selectedYearMonth by Delegates.notNull<Int>()
    }
}