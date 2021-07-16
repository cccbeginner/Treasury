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

    suspend fun fetchData(yearMonth: Int, type: Int){
        println("Fetch Data $yearMonth $type")
        val forms = formDao.getByType(selectedYearMonth, type)
        val formArray = forms.toCollection(ArrayList())
        formArrayFlowMap[yearMonth - MyApplication.start][type].emit(formArray)
    }

    suspend fun insert(form: Form): Boolean {
        println("Insert $form")
        return formDao.insert(form)
    }
    suspend fun update(form: Form){
        println("Update $form")
        formDao.update(form)
    }
    suspend fun delete(form: Form){
        println("delete $form")
        formDao.delete(form)
    }

    companion object{
        var selectedYearMonth by Delegates.notNull<Int>()
    }
}