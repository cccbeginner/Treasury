package com.example.treasury.edit

import androidx.lifecycle.*
import com.example.treasury.formDatabase.FormRepository
import com.example.treasury.MyApplication
import com.example.treasury.formDatabase.Form
import kotlinx.coroutines.launch

class EditViewModel(private val formRepository: FormRepository, yearMonth: Int) : ViewModel() {
    private val numOfArray = 6
    var formLiveDataArray = ArrayList<LiveData<ArrayList<Form>>>()

    // tmp arrays store temporary data
    var tmpFormLiveDataArray = ArrayList<MutableLiveData<ArrayList<Form>>>()
    private var tmpFormArray = ArrayList<ArrayList<Form>>()

    init {
        val array = formRepository.formArrayFlowMap[yearMonth - MyApplication.start]
        for(i in 0 until numOfArray){
            formLiveDataArray.add(array[i].asLiveData())
            tmpFormLiveDataArray.add(MutableLiveData(ArrayList()))
            tmpFormArray.add(ArrayList())
        }
    }

    // post value to the livedata array
    private fun updateLiveData(type: Int){
        tmpFormLiveDataArray[type].postValue(tmpFormArray[type])
    }

    // insert, update delete are just save data
    // in local variable temporarily
    fun insert(form: Form): Boolean{
        for (curForm in tmpFormArray[form.type]){
            if (form.name == curForm.name){
                return false
            }
        }
        tmpFormArray[form.type].add(form)
        updateLiveData(form.type)
        return true
    }
    fun update(form: Form){
        val arraySize = tmpFormArray[form.type].size
        for (i in 0 until arraySize){
            val curForm = tmpFormArray[form.type][i]
            if (form.name == curForm.name){
                tmpFormArray[form.type][i] = form
            }
        }
        updateLiveData(form.type)
    }
    fun delete(form: Form){
        tmpFormArray[form.type].remove(form)
        updateLiveData(form.type)
    }

    // save to database
    fun saveData(){
        viewModelScope.launch {
            formRepository.deleteByCurrentYearMonth()
            for (formArray in tmpFormArray){
                formRepository.insertMany(formArray)
            }
            formRepository.fetchData()
        }
    }
}

class EditViewModelFactory(private val formRepository: FormRepository, private val yearMonth: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditViewModel(formRepository, yearMonth) as T
    }
}