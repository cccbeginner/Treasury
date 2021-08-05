package com.example.treasury.edit

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treasury.formDatabase.FormRepository
import com.example.treasury.MyApplication
import com.example.treasury.R
import com.example.treasury.formDatabase.Form

class EditActivity : AppCompatActivity() {

    private lateinit var formRepository : FormRepository
    private lateinit var editViewModel : EditViewModel
    private var currentYearMonth = -1

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        currentYearMonth = intent.getIntExtra("yearMonth", -1)
        formRepository = (application as MyApplication).formRepository
        editViewModel = ViewModelProvider(this, EditViewModelFactory(formRepository,
            currentYearMonth
        )).get(
            EditViewModel::class.java)


        /*
         * Set views.
         */
        val viewArray = mutableMapOf<Int, View>()
        viewArray[Form.type_1] = findViewById(R.id.type_1)
        viewArray[Form.type_1_1] = findViewById(R.id.type_1_1)
        viewArray[Form.type_1_2] = findViewById(R.id.type_1_2)
        viewArray[Form.type_1_3] = findViewById(R.id.type_1_3)
        viewArray[Form.type_2] = findViewById(R.id.type_2)
        viewArray[Form.type_2_1] = findViewById(R.id.type_2_1)
        viewArray[Form.type_2_2] = findViewById(R.id.type_2_2)
        viewArray[Form.type_2_3] = findViewById(R.id.type_2_3)
        viewArray[Form.type_3] = findViewById(R.id.type_3)
        viewArray[Form.type_4] = findViewById(R.id.type_4)
        viewArray[Form.type_5] = findViewById(R.id.type_5)
        viewArray[Form.type_ex_rate] = findViewById(R.id.type_ex_rate)
        viewArray[Form.type_6] = findViewById(R.id.type_6)
        viewArray[Form.type_7] = findViewById(R.id.type_7)

        /*
         * Set view paddings
         * (100 dp start)
         */
        viewArray[Form.type_1_1]!!.setPadding(100, 20, 0, 0)
        viewArray[Form.type_1_2]!!.setPadding(100, 0, 0, 0)
        viewArray[Form.type_1_3]!!.setPadding(100, 0, 0, 0)
        viewArray[Form.type_2_1]!!.setPadding(100, 20, 0, 0)
        viewArray[Form.type_2_2]!!.setPadding(100, 20, 0, 0)
        viewArray[Form.type_2_3]!!.setPadding(100, 0, 0, 0)
        viewArray[Form.type_1]!!.setPadding(0, 20, 0, 0)
        viewArray[Form.type_2]!!.setPadding(0, 20, 0, 0)
        viewArray[Form.type_3]!!.setPadding(0, 20, 0, 0)
        viewArray[Form.type_4]!!.setPadding(0, 20, 0, 0)
        viewArray[Form.type_5]!!.setPadding(0, 20, 0, 0)
        viewArray[Form.type_ex_rate]!!.setPadding(50, 0, 0, 0)
        viewArray[Form.type_6]!!.setPadding(0, 20, 0, 0)
        viewArray[Form.type_7]!!.setPadding(0, 20, 0, 0)


        /*
         * Set titles for all types
         */
        viewArray[Form.type_1]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title1)
        viewArray[Form.type_1_1]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title1_1)
        viewArray[Form.type_1_2]!!.findViewById<TextView>(R.id.title_edit).text = getString(R.string.title1_2)
        viewArray[Form.type_1_3]!!.findViewById<TextView>(R.id.title_edit).text = getString(R.string.title1_3)
        viewArray[Form.type_2]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title2)
        viewArray[Form.type_2_1]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title2_1)
        viewArray[Form.type_2_2]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title2_2)
        viewArray[Form.type_2_3]!!.findViewById<TextView>(R.id.title_edit).text = getString(R.string.title2_3)
        viewArray[Form.type_3]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title3)
        viewArray[Form.type_4]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title4)
        viewArray[Form.type_5]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title5)
        viewArray[Form.type_ex_rate]!!.findViewById<TextView>(R.id.title_edit).text = getString(R.string.title_ex_rate)
        viewArray[Form.type_6]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title6)
        viewArray[Form.type_7]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title7)


        /*
         * Define recyclerview arrays.
         * Set type with a list of data like:
         *     type_1_1, type_2_1, type_2_2, type_3, type_4, type_5
         */
        val formRecyclerViewArray = mutableMapOf<Int, RecyclerView>()
        formRecyclerViewArray[Form.type_1_1] = findViewById(R.id.form_1_1_recyclerview)
        formRecyclerViewArray[Form.type_2_1] = findViewById(R.id.form_2_1_recyclerview)
        formRecyclerViewArray[Form.type_2_2] = findViewById(R.id.form_2_2_recyclerview)
        formRecyclerViewArray[Form.type_3] = findViewById(R.id.form_3_recyclerview)
        formRecyclerViewArray[Form.type_4] = findViewById(R.id.form_4_recyclerview)
        formRecyclerViewArray[Form.type_5] = findViewById(R.id.form_5_recyclerview)

        formRecyclerViewArray[Form.type_1_1]!!.setPadding(200, 0, 0, 0)
        formRecyclerViewArray[Form.type_2_1]!!.setPadding(200, 0, 0, 0)
        formRecyclerViewArray[Form.type_2_2]!!.setPadding(200, 0, 0, 0)
        formRecyclerViewArray[Form.type_3]!!.setPadding(100, 0, 0, 0)
        formRecyclerViewArray[Form.type_4]!!.setPadding(100, 0, 0, 0)
        formRecyclerViewArray[Form.type_5]!!.setPadding(100, 0, 0, 0)

        /*
         * Implement adapters and observers for lists of data.
         */
        for(type in Form.listTypeArray){
            val adapter = EditFormAdapter(ArrayList())
            adapter.event = TextChange(editViewModel)
            formRecyclerViewArray[type]!!.adapter = adapter
            formRecyclerViewArray[type]!!.layoutManager = LinearLayoutManager(applicationContext)
            editViewModel.formLiveDataArray[type]?.observe(this, {
                /*
                 * What this observer do is to fetch database data
                 * initially, and it supposed to do only once.
                 */
                for (form in it){
                    editViewModel.insert(form)
                }
            })
            editViewModel.tmpFormLiveDataArray[type]?.observe(this, {
                runOnUiThread {
                    adapter.updateData(it)
                }
            })
        }

        /*
         * Implements observers for calculating sum.
         */
        for (type in Form.sumTypeArray){
            if(type in Form.singleTypeArray)continue
            editViewModel.tmpSumLiveDataArray[type]!!.observe(this, {
                viewArray[type]!!.findViewById<TextView>(R.id.number_show).text = it.toString()
            })
        }

        /*
         * Set button for inserting element.
         */
        val addButtonArray = mutableMapOf<Int, Button>()
        addButtonArray[Form.type_1_1] = findViewById(R.id.add_1_1_button)
        addButtonArray[Form.type_2_1] = findViewById(R.id.add_2_1_button)
        addButtonArray[Form.type_2_2] = findViewById(R.id.add_2_2_button)
        addButtonArray[Form.type_3] = findViewById(R.id.add_3_button)
        addButtonArray[Form.type_4] = findViewById(R.id.add_4_button)
        addButtonArray[Form.type_5] = findViewById(R.id.add_5_button)
        for (type in Form.listTypeArray){
            addButtonArray[type]?.setOnClickListener {
                insertDialog(this, type)
            }
        }

        /*
         * Set type with single data like:
         *     type_1_2, type_1_3, type_2_3
         */
        val editTextArray = mutableMapOf<Int, EditText>()
        editTextArray[Form.type_1_2] = viewArray[Form.type_1_2]!!.findViewById(R.id.number_edit)
        editTextArray[Form.type_1_3] = viewArray[Form.type_1_3]!!.findViewById(R.id.number_edit)
        editTextArray[Form.type_2_3] = viewArray[Form.type_2_3]!!.findViewById(R.id.number_edit)
        editTextArray[Form.type_ex_rate] = viewArray[Form.type_ex_rate]!!.findViewById(R.id.number_edit)
        val nameMap = mapOf(Form.type_1_2 to "現金", Form.type_1_3 to "外幣", Form.type_2_3 to "黃金", Form.type_ex_rate to "美股匯率")
        for (type in Form.singleTypeArray){
            editViewModel.formLiveDataArray[type]!!.observe(this, {
                /*
                 * What this observer do is to fetch database data
                 * initially, and it supposed to do only once.
                 */
                if(it.isNotEmpty()) {
                    editViewModel.insert(it[0])
                    editTextArray[type]!!.setText(it[0].money)
                }
            })
            editTextArray[type]!!.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    editViewModel.update(type, nameMap[type]!!, s.toString())
                }
            })
        }

        /*
         * Set date.
         */
        var alreadyHaveDate = false
        val viewDate = findViewById<View>(R.id.date)
        viewDate.setPadding(0, 20, 0, 40)
        val dateIdMap = mapOf(Form.type_year to R.id.year_edit, Form.type_month to R.id.month_edit, Form.type_day to R.id.day_edit)
        val dateNameMap = mapOf(Form.type_year to "西元年", Form.type_month to "月", Form.type_day to "日")
        for (type in Form.dateTypeArray) {
            editViewModel.formLiveDataArray[type]!!.observe(this, {
                if (it.isNotEmpty()) {
                    editViewModel.insert(it[0])
                    viewDate.findViewById<TextView>(dateIdMap[type]!!).text = it[0].money
                }else if (!alreadyHaveDate){
                    initColumns()
                    alreadyHaveDate = true
                }
            })
            viewDate.findViewById<TextView>(dateIdMap[type]!!).addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    editViewModel.update(type, dateNameMap[type]!!, s.toString())
                }
            })
        }

        /*
         * save button & cancel button
         */
        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener{
            editViewModel.saveData()
            finish()
        }
        val cancelButton = findViewById<Button>(R.id.cancel_button)
        cancelButton.setOnClickListener{
            finish()
        }
    }

    /*
     * Call this function if there is nothing in the form.
     */
    private fun initColumns(){

        for (type in Form.dataTypeArray){
            editViewModel.formLiveDataArrayExtra[type]!!.observe(this, {
                val arrayList = arrayListOf<String>()
                for (form in it){
                    arrayList.add(form.name)
                }
                for(name in arrayList){
                    editViewModel.insert(Form(currentYearMonth, type, name))
                }
            })
        }
        val nameMap = mapOf(
            Form.type_1_2 to "現金",
            Form.type_1_3 to "外幣",
            Form.type_2_3 to "黃金",
            Form.type_ex_rate to "美股匯率",
            Form.type_year to "西元年",
            Form.type_month to "月",
            Form.type_day to "日"
        )
        for(type in Form.dataTypeArray){
            if(type in Form.listTypeArray) {
                editViewModel.formLiveDataArrayExtra[type]!!.observe(this, {
                    val arrayList = arrayListOf<String>()
                    for (form in it) {
                        arrayList.add(form.name)
                    }
                    for (name in arrayList) {
                        editViewModel.insert(Form(currentYearMonth, type, name))
                    }
                })
            }else{
                editViewModel.insert(Form(currentYearMonth, type, nameMap[type]!!))
            }
        }
    }

    private class TextChange(val editViewModel : EditViewModel) : EditFormAdapter.Event {

        override fun onMoneyChange(number: String, form: Form) {
            editViewModel.update(form.type, form.name, number)
        }

        override fun onFormDelete(form: Form) {
            editViewModel.delete(form)
        }
    }

    private fun insertDialog(context: Context, formType: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val editText = EditText(context) //final一個editText
        builder.setView(editText)
        builder.setTitle("輸入名稱")
        builder.setPositiveButton("確定") { _, _ ->
            val title = editText.text.toString().replace("\\s+".toRegex(), " ")
            println(title)
            if (title != "" && title != " "){
                val newForm = Form(currentYearMonth, formType, title)
                editViewModel.insert(newForm)
            }
        }
        builder.setNegativeButton("取消") { _, _ ->}
        builder.create().show()
    }
}