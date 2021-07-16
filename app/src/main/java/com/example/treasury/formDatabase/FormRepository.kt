package com.example.treasury.formDatabase

import com.example.treasury.MyApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.Delegates

class FormRepository (private val formDao: FormDao) {
    private val arraySize = 6

    // mutable map <yearMonth, data>
    val formArrayFlowMap = ArrayList< ArrayList< MutableStateFlow< ArrayList<Form> > > >()

    init {
        for(yearMonth in MyApplication.start..MyApplication.end){
            val arrayList = ArrayList< MutableStateFlow< ArrayList<Form> > >()
            for(i in 0 until arraySize){
                arrayList.add(MutableStateFlow(ArrayList()))
            }
            formArrayFlowMap.add(arrayList)
        }
    }

    suspend fun fetchData(){
        for (type in 0 until arraySize) {
            val forms = formDao.getByType(selectedYearMonth, type)
            val formArray = forms.toCollection(ArrayList())
            formArrayFlowMap[selectedYearMonth - MyApplication.start][type].emit(formArray)
        }
    }

    suspend fun insertMany(formArrayList: ArrayList<Form>){
        println(formArrayList)
        formDao.insertMany(formArrayList.toTypedArray())
    }
    suspend fun deleteByCurrentYearMonth(){
        formDao.deleteByYearMonth(selectedYearMonth)
    }

    companion object{
        var selectedYearMonth by Delegates.notNull<Int>()
    }
}