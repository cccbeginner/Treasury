package com.example.treasury.page

import androidx.lifecycle.*
import com.example.treasury.formDatabase.FormRepository
import com.example.treasury.formDatabase.Form
import kotlinx.coroutines.launch

class PageViewModel(private val formRepository: FormRepository, private val yearMonth: Int) : ViewModel() {
    var formLiveDataArray = mutableMapOf< Int, LiveData< ArrayList< Form > > >()
    var sumLiveDataArray = mutableMapOf< Int, MutableLiveData< Double > >()

    init {
        // fill empty stuffs to the arrays above
        for (type in Form.sumTypeArray){
            sumLiveDataArray[type] = MutableLiveData(0.toDouble())
        }
        val listFormArray = formRepository.listFlowMap
        for (type in Form.dataTypeArray){
            formLiveDataArray[type] = listFormArray[type]!!.asLiveData()
        }
    }

    fun fetchData(){
        viewModelScope.launch {
            formRepository.fetchData(yearMonth)
        }
    }

    fun updateSum(type: Int){
        var sum = 0.toDouble()
        if (type in Form.dataTypeArray) {
            for (form in formLiveDataArray[type]!!.value!!) {
                if(form.money != ""){
                    sum += form.money.toDouble()
                }
            }
        }
        when(type) {
            Form.type_1 -> {
                sum = sumLiveDataArray[Form.type_1_1]!!.value!!
                sum += sumLiveDataArray[Form.type_1_2]!!.value!!
                sum += sumLiveDataArray[Form.type_1_3]!!.value!!
            }
            Form.type_2 -> {
                sum = sumLiveDataArray[Form.type_2_1]!!.value!!
                sum += sumLiveDataArray[Form.type_2_2]!!.value!!
                sum += sumLiveDataArray[Form.type_2_3]!!.value!!
            }
            Form.type_5 -> {
                sum *= sumLiveDataArray[Form.type_ex_rate]!!.value!!
            }
            Form.type_6 -> {
                sum = sumLiveDataArray[Form.type_1]!!.value!!
                sum += sumLiveDataArray[Form.type_2]!!.value!!
                sum -= sumLiveDataArray[Form.type_3]!!.value!!
                sum -= sumLiveDataArray[Form.type_4]!!.value!!
            }
            Form.type_7 -> {
                sum = sumLiveDataArray[Form.type_5]!!.value!!
                sum += sumLiveDataArray[Form.type_6]!!.value!!
            }
        }
        sumLiveDataArray[type]!!.postValue(sum)
    }
}

class PageViewModelFactory(private val formRepository: FormRepository, private val yearMonth: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PageViewModel(formRepository, yearMonth) as T
    }
}