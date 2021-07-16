package com.example.treasury.page

import androidx.lifecycle.*
import com.example.treasury.formDatabase.FormRepository
import com.example.treasury.MyApplication
import com.example.treasury.formDatabase.Form
import kotlinx.coroutines.launch

class PageViewModel(private val formRepository: FormRepository, private val yearMonth: Int) : ViewModel() {
    private val arraySize = 6
    var formLiveDataArray = ArrayList< LiveData< ArrayList<Form> > >()

    init {
        val array = formRepository.formArrayFlowMap[yearMonth - MyApplication.start]
        for(i in 0 until arraySize){
            formLiveDataArray.add(array[i].asLiveData())
        }
    }

    fun fetchData(){
        viewModelScope.launch {
            for(i in 0 until arraySize){
                formRepository.fetchData(yearMonth, i)
            }
        }
    }
}

class PageViewModelFactory(private val formRepository: FormRepository, private val yearMonth: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PageViewModel(formRepository, yearMonth) as T
    }
}