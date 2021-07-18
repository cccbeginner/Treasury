package com.example.treasury.edit

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        formRepository = (application as MyApplication).formRepository
        editViewModel = ViewModelProvider(this, EditViewModelFactory(formRepository,
            FormRepository.selectedYearMonth
        )).get(
            EditViewModel::class.java)

        val formRecyclerViewArray = mutableMapOf<Int, RecyclerView>()
        formRecyclerViewArray[Form.type_1_1] = findViewById(R.id.form_1_1_recyclerview)
        formRecyclerViewArray[Form.type_2_1] = findViewById(R.id.form_2_1_recyclerview)
        formRecyclerViewArray[Form.type_2_2] = findViewById(R.id.form_2_2_recyclerview)
        formRecyclerViewArray[Form.type_3] = findViewById(R.id.form_3_recyclerview)
        formRecyclerViewArray[Form.type_4] = findViewById(R.id.form_4_recyclerview)
        formRecyclerViewArray[Form.type_5] = findViewById(R.id.form_5_recyclerview)

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
                if(alreadyObserve == formRecyclerViewArray.size){
                    initData()
                }
            })
            editViewModel.tmpFormLiveDataArray[type]?.observe(this, {
                runOnUiThread {
                    adapter.updateData(it)
                }
            })
        }

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

        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener{
            editViewModel.saveData()
            finish()
        }

    }

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
            Form.type_2_1 to arrayOf("富邦銀", "富邦證", "元大證"),
            Form.type_2_2 to arrayOf("台銀", "星展"),
            Form.type_3 to arrayOf(),
            Form.type_4 to arrayOf("文昌押金"),
            Form.type_5 to arrayOf("E trade 舊", "E trade 新")
        )
        for(i in Form.listTypeArray){
            for(name in insertArray[i]!!){
                editViewModel.insert(Form(month, i, name))
            }
        }
    }

    private class TextChange(val editViewModel : EditViewModel) : EditFormAdapter.Event {

        override fun onMoneyChange(number: Long, form: Form) {
            form.money = number
            editViewModel.update(form)
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