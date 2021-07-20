package com.example.treasury.edit

import androidx.lifecycle.*
import com.example.treasury.formDatabase.FormRepository
import com.example.treasury.formDatabase.Form
import kotlinx.coroutines.launch

class EditViewModel(private val formRepository: FormRepository, yearMonth: Int) : ViewModel() {
    var formLiveDataArray = mutableMapOf< Int, LiveData< ArrayList< Form > > >()

    // tmp arrays store temporary data
    var tmpFormLiveDataArray = mutableMapOf< Int, MutableLiveData< ArrayList< Form > > >()
    var tmpSumLiveDataArray = mutableMapOf< Int, MutableLiveData< Long > >()

    private var tmpFormArray = mutableMapOf< Int, ArrayList<Form> >()
    private var tmpSumArray = mutableMapOf< Int, Long >()

    init {
        // fill empty stuffs to the arrays above
        val listFormArray = formRepository.listFlowMap[yearMonth]!!
        for (type in Form.dataTypeArray){
            formLiveDataArray[type] = listFormArray[type]?.asLiveData()!!
            tmpFormLiveDataArray[type] = MutableLiveData(ArrayList())
            tmpFormArray[type] = ArrayList()
        }
        for (type in Form.allTypeArray){
            tmpSumLiveDataArray[type] = MutableLiveData(0)
            tmpSumArray[type] = 0
        }
    }

    // post value to the livedata array
    private fun updateLiveData(type: Int){
        if(type in Form.dataTypeArray){
            tmpFormLiveDataArray[type]?.postValue(tmpFormArray[type])
        }
    }

    // insert, update delete are just save data
    // in local variable temporarily
    fun insert(form: Form): Boolean{
        for (curForm in tmpFormArray[form.type]!!){
            if (form.name == curForm.name){
                return false
            }
        }
        tmpFormArray[form.type]!!.add(form)
        updateLiveData(form.type)
        updateSum(form.type)
        return true
    }
    fun update(type: Int, name: String, money: Long){
        for (i in 0 until tmpFormArray[type]!!.size){
            val curForm = tmpFormArray[type]!![i]
            if (name == curForm.name){
                tmpFormArray[type]!![i].money = money
            }
        }
        updateLiveData(type)
        updateSum(type)
    }
    fun delete(form: Form){
        tmpFormArray[form.type]!!.remove(form)
        updateLiveData(form.type)
        updateSum(form.type)
    }

    // save to database
    fun saveData(){
        viewModelScope.launch {
            formRepository.deleteByCurrentYearMonth()
            for (formArray in tmpFormArray){
                formRepository.insertMany(formArray.value)
            }
            formRepository.fetchData()
        }
    }

    private fun updateSum(type: Int){
        var sum = 0L
        if(type in Form.dataTypeArray){
            for (form in tmpFormArray[type]!!){
                sum += form.money
            }
        }
        when(type) {
            Form.type_1 -> {
                sum = tmpSumArray[Form.type_1_1]!!
                sum += tmpSumArray[Form.type_1_2]!!
                sum += tmpSumArray[Form.type_1_3]!!
            }
            Form.type_2 -> {
                sum = tmpSumArray[Form.type_2_1]!!
                sum += tmpSumArray[Form.type_2_2]!!
                sum += tmpSumArray[Form.type_2_3]!!
            }
            Form.type_6 -> {
                sum = tmpSumArray[Form.type_1]!!
                sum += tmpSumArray[Form.type_2]!!
                sum -= tmpSumArray[Form.type_3]!!
                sum -= tmpSumArray[Form.type_4]!!
            }
            Form.type_7 -> {
                //sum = tmpSumArray[Form.type_5]!! * rate
                sum = tmpSumArray[Form.type_5]!!
                sum += tmpSumArray[Form.type_6]!!
            }
        }
        tmpSumArray[type] = sum
        tmpSumLiveDataArray[type]!!.postValue(sum)
        when(type) {
            Form.type_1_1 -> updateSum(Form.type_1)
            Form.type_1_2 -> updateSum(Form.type_1)
            Form.type_1_3 -> updateSum(Form.type_1)
            Form.type_2_1 -> updateSum(Form.type_2)
            Form.type_2_2 -> updateSum(Form.type_2)
            Form.type_2_3 -> updateSum(Form.type_2)
            Form.type_1 -> updateSum(Form.type_6)
            Form.type_2 -> updateSum(Form.type_6)
            Form.type_3 -> updateSum(Form.type_6)
            Form.type_4 -> updateSum(Form.type_6)
            Form.type_5 -> updateSum(Form.type_7)
            Form.type_6 -> updateSum(Form.type_7)
        }
    }
}

class EditViewModelFactory(private val formRepository: FormRepository, private val yearMonth: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditViewModel(formRepository, yearMonth) as T
    }
}