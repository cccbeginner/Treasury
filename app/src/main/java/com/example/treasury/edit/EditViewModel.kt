package com.example.treasury.edit

import androidx.lifecycle.*
import com.example.treasury.formDatabase.FormRepository
import com.example.treasury.MyApplication
import com.example.treasury.formDatabase.Form
import kotlinx.coroutines.launch

class EditViewModel(private val formRepository: FormRepository, private val yearMonth: Int) : ViewModel() {
    private val arraySize = 6
    var formLiveDataArray = ArrayList<LiveData<ArrayList<Form>>>()

    init {
        val array = formRepository.formArrayFlowMap[yearMonth - MyApplication.start]
        for(i in 0 until arraySize){
            formLiveDataArray.add(array[i].asLiveData())
        }
    }

    fun insert(form: Form){
        viewModelScope.launch{
            formRepository.insert(form)
            formRepository.fetchData(yearMonth, form.type)
        }
    }
    fun update(form: Form){
        viewModelScope.launch{
            formRepository.update(form)
            formRepository.fetchData(yearMonth, form.type)
        }
    }
    fun delete(form: Form){
        viewModelScope.launch{
            formRepository.delete(form)
            formRepository.fetchData(yearMonth, form.type)
        }
    }
}

class EditViewModelFactory(private val formRepository: FormRepository, private val yearMonth: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditViewModel(formRepository, yearMonth) as T
    }
}