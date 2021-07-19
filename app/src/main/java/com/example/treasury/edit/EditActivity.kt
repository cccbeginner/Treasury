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

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        formRepository = (application as MyApplication).formRepository
        editViewModel = ViewModelProvider(this, EditViewModelFactory(formRepository,
            FormRepository.selectedYearMonth
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
        viewArray[Form.type_6] = findViewById(R.id.type_6)
        viewArray[Form.type_7] = findViewById(R.id.type_7)

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


        /*
         * Implement adapters and observers r lists of data.
         */
        var alreadyObserve = 0
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
                alreadyObserve += 1
                if(alreadyObserve == Form.dataTypeArray.size){
                    initData()
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
        for (type in Form.allTypeArray){
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
        addButtonArray[Form.type_3] = findViewById(R.id.add_3_button)
        addButtonArray[Form.type_4] = findViewById(R.id.add_4_button)
        addButtonArray[Form.type_5] = findViewById(R.id.add_5_button)
        for (type in Form.listTypeArray){
            if (type == Form.type_2_2)continue
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
        val nameMap = mapOf(Form.type_1_2 to "現金", Form.type_1_3 to "外幣", Form.type_2_3 to "黃金")
        for (type in Form.singleTypeArray){
            editViewModel.formLiveDataArray[type]!!.observe(this, {
                /*
                 * What this observer do is to fetch database data
                 * initially, and it supposed to do only once.
                 */
                for (form in it){
                    editViewModel.insert(form)
                }
                alreadyObserve += 1
                if(alreadyObserve == Form.dataTypeArray.size){
                    initData()
                }
            })
            editViewModel.tmpFormLiveDataArray[type]!!.observe(this, {
                if(it.isNotEmpty()) {
                    if (it[0].money != 0L) {
                        editTextArray[type]!!.setText(it[0].money.toString())
                    }
                }
            })
            editTextArray[type]!!.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if(s.toString() == ""){
                        editViewModel.update(type, nameMap[type]!!, 0)
                    }else{
                        editViewModel.update(type, nameMap[type]!!, s.toString().toLong())
                    }
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
    private fun initData(){
        /*
        initialize forms if doesn't have any:

        年/月/日

        一、 流動現金：(a+b+c)
        a. 活存 :
        中山 台山
        台茹 台宴
        郵茹 星展
        b. 現金
        c. 外幣

        二、投資帳現值：
        a. 證券
        富邦銀
        富邦證
        元大證
        b. 基金
        台銀
        星展
        c. 黃金

        三、貸款餘額 :
        a. 備註
        b. 備註

        四、扣除 : 文昌押金

        五、美股
        a. E trade 舊
        b. E trade 新
        c. F trade
        註. 美金對台幣匯率

        六、不含美股總值 (一 + 二 - 三 - 四)

        七、含美股總值 (五×匯率＋六)
        (美股總值折合台幣=五×匯率)

         */
        val month = FormRepository.selectedYearMonth
        val insertArray = mapOf(
            Form.type_1_1 to arrayOf("中山", "台山", "台茹", "台宴", "郵茹", "星展"),
            Form.type_1_2 to arrayOf("現金"),
            Form.type_1_3 to arrayOf("外幣"),
            Form.type_2_1 to arrayOf("富邦銀", "富邦證", "元大證"),
            Form.type_2_2 to arrayOf("台銀", "星展"),
            Form.type_2_3 to arrayOf("黃金"),
            Form.type_3 to arrayOf(),
            Form.type_4 to arrayOf("文昌押金"),
            Form.type_5 to arrayOf("E trade 舊", "E trade 新")
        )
        for(i in Form.dataTypeArray){
            for(name in insertArray[i]!!){
                editViewModel.insert(Form(month, i, name))
            }
        }
    }

    private class TextChange(val editViewModel : EditViewModel) : EditFormAdapter.Event {

        override fun onMoneyChange(number: Long, form: Form) {
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
                val newForm = Form(FormRepository.selectedYearMonth, formType, title)
                editViewModel.insert(newForm)
            }
        }
        builder.setNegativeButton("取消") { _, _ ->}
        builder.create().show()
    }
}