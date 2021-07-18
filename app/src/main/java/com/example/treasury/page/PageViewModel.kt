package com.example.treasury.page

import androidx.lifecycle.*
import com.example.treasury.formDatabase.FormRepository
import com.example.treasury.formDatabase.Form
import kotlinx.coroutines.launch

class PageViewModel(private val formRepository: FormRepository, yearMonth: Int) : ViewModel() {
    var formLiveDataArray = mutableMapOf< Int, LiveData< ArrayList< Form > > >()

    init {
        // fill empty stuffs to the arrays above
        val listFormArray = formRepository.listFlowMap[yearMonth]!!
        for (type in Form.dataTypeArray){
            formLiveDataArray[type] = listFormArray[type]?.asLiveData()!!
        }
    }

    fun fetchData(){
        viewModelScope.launch {
            formRepository.fetchData()
        }
    }
}

class PageViewModelFactory(private val formRepository: FormRepository, private val yearMonth: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PageViewModel(formRepository, yearMonth) as T
    }
}