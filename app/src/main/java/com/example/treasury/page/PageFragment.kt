package com.example.treasury.page

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treasury.MyApplication
import com.example.treasury.R
import com.example.treasury.edit.EditActivity
import com.example.treasury.formDatabase.Form
import com.example.treasury.formDatabase.FormRepository

class PageFragment(private val yearMonth: Int) : Fragment() {

    private lateinit var formRepository: FormRepository
    private lateinit var pageViewModel: PageViewModel
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // initialize view model
        formRepository = (requireActivity().application as MyApplication).formRepository
        pageViewModel = ViewModelProvider(this, PageViewModelFactory(formRepository, yearMonth)).get(
            PageViewModel::class.java)

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_page, container, false)

        val page = root.findViewById<LinearLayout>(R.id.form_page)
        val goEdit = root.findViewById<ImageButton>(R.id.go_edit_button)
        val goEdit2 = root.findViewById<Button>(R.id.go_edit_button_2)
        goEdit.visibility = View.VISIBLE
        page.visibility = View.GONE

        /*
         * Set views.
         */
        val viewArray = mutableMapOf<Int, View>()
        viewArray[Form.type_1] = root.findViewById(R.id.type_1)
        viewArray[Form.type_1_1] = root.findViewById(R.id.type_1_1)
        viewArray[Form.type_1_2] = root.findViewById(R.id.type_1_2)
        viewArray[Form.type_1_3] = root.findViewById(R.id.type_1_3)
        viewArray[Form.type_2] = root.findViewById(R.id.type_2)
        viewArray[Form.type_2_1] = root.findViewById(R.id.type_2_1)
        viewArray[Form.type_2_2] = root.findViewById(R.id.type_2_2)
        viewArray[Form.type_2_3] = root.findViewById(R.id.type_2_3)
        viewArray[Form.type_3] = root.findViewById(R.id.type_3)
        viewArray[Form.type_4] = root.findViewById(R.id.type_4)
        viewArray[Form.type_5] = root.findViewById(R.id.type_5)
        viewArray[Form.type_ex_rate] = root.findViewById(R.id.type_ex_rate)
        viewArray[Form.type_6] = root.findViewById(R.id.type_6)
        viewArray[Form.type_7] = root.findViewById(R.id.type_7)

        /*
         * Set view paddings
         * (40 dp start)
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
        viewArray[Form.type_1_2]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title1_2)
        viewArray[Form.type_1_3]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title1_3)
        viewArray[Form.type_2]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title2)
        viewArray[Form.type_2_1]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title2_1)
        viewArray[Form.type_2_2]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title2_2)
        viewArray[Form.type_2_3]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title2_3)
        viewArray[Form.type_3]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title3)
        viewArray[Form.type_4]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title4)
        viewArray[Form.type_5]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title5)
        viewArray[Form.type_ex_rate]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title_ex_rate)
        viewArray[Form.type_6]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title6)
        viewArray[Form.type_7]!!.findViewById<TextView>(R.id.title_show).text = getString(R.string.title7)

        // set adapters for listed data
        val formRecyclerViewArray = mutableMapOf<Int, RecyclerView>()
        formRecyclerViewArray[Form.type_1_1] = root.findViewById(R.id.form_1_1_recyclerview)
        formRecyclerViewArray[Form.type_2_1] = root.findViewById(R.id.form_2_1_recyclerview)
        formRecyclerViewArray[Form.type_2_2] = root.findViewById(R.id.form_2_2_recyclerview)
        formRecyclerViewArray[Form.type_3] = root.findViewById(R.id.form_3_recyclerview)
        formRecyclerViewArray[Form.type_4] = root.findViewById(R.id.form_4_recyclerview)
        formRecyclerViewArray[Form.type_5] = root.findViewById(R.id.form_5_recyclerview)

        formRecyclerViewArray[Form.type_1_1]!!.setPadding(200, 0, 0, 0)
        formRecyclerViewArray[Form.type_2_1]!!.setPadding(200, 0, 0, 0)
        formRecyclerViewArray[Form.type_2_2]!!.setPadding(200, 0, 0, 0)
        formRecyclerViewArray[Form.type_3]!!.setPadding(100, 0, 0, 0)
        formRecyclerViewArray[Form.type_4]!!.setPadding(100, 0, 0, 0)
        formRecyclerViewArray[Form.type_5]!!.setPadding(100, 0, 0, 0)

        for(type in Form.listTypeArray){
            val adapter = PageFormAdapter(ArrayList())
            formRecyclerViewArray[type]?.adapter = adapter
            formRecyclerViewArray[type]?.layoutManager = LinearLayoutManager(requireContext())
            pageViewModel.formLiveDataArray[type]?.observe(viewLifecycleOwner, {
                requireActivity().runOnUiThread {
                    adapter.updateData(it)
                    if (it.isNotEmpty() && it[0].yearMonth == yearMonth) {
                        goEdit.visibility = View.GONE
                        page.visibility = View.VISIBLE
                    }
                }
                pageViewModel.updateSum(type)
            })
        }

        for (type in Form.singleTypeArray){
            pageViewModel.formLiveDataArray[type]!!.observe(viewLifecycleOwner, {
                if (it.isNotEmpty()) {
                    viewArray[type]!!.findViewById<TextView>(R.id.number_show).text =
                        it[0].money
                }
                pageViewModel.updateSum(type)
            })
        }

        /*
         * Implements observers for showing sum.
         */
        for (type in Form.allTypeArray){
            pageViewModel.sumLiveDataArray[type]!!.observe(viewLifecycleOwner, {
                if(type !in Form.singleTypeArray) {
                    viewArray[type]!!.findViewById<TextView>(R.id.number_show).text = it.toString()
                }
                when(type) {
                    Form.type_1_1 -> pageViewModel.updateSum(Form.type_1)
                    Form.type_1_2 -> pageViewModel.updateSum(Form.type_1)
                    Form.type_1_3 -> pageViewModel.updateSum(Form.type_1)
                    Form.type_2_1 -> pageViewModel.updateSum(Form.type_2)
                    Form.type_2_2 -> pageViewModel.updateSum(Form.type_2)
                    Form.type_2_3 -> pageViewModel.updateSum(Form.type_2)
                    Form.type_ex_rate -> pageViewModel.updateSum(Form.type_5)
                    Form.type_1 -> pageViewModel.updateSum(Form.type_6)
                    Form.type_2 -> pageViewModel.updateSum(Form.type_6)
                    Form.type_3 -> pageViewModel.updateSum(Form.type_6)
                    Form.type_4 -> pageViewModel.updateSum(Form.type_6)
                    Form.type_5 -> pageViewModel.updateSum(Form.type_7)
                    Form.type_6 -> pageViewModel.updateSum(Form.type_7)
                }
            })
        }

        // get data at the beginning
        pageViewModel.fetchData()

        // go edit page
        goEdit.setOnClickListener{
            val intent = Intent(requireActivity(), EditActivity::class.java)
            intent.putExtra("yearMonth", yearMonth)
            startActivity(intent)
        }
        goEdit2.setOnClickListener{
            val intent = Intent(requireActivity(), EditActivity::class.java)
            intent.putExtra("yearMonth", yearMonth)
            startActivity(intent)
        }

        println("page fragment ${yearMonth / 12} / ${yearMonth % 12 + 1}")
        return root
    }
}