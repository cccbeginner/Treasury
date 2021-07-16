package com.example.treasury.page

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treasury.edit.EditActivity
import com.example.treasury.formDatabase.FormRepository
import com.example.treasury.MyApplication
import com.example.treasury.R

class PageFragment(private val yearMonth: Int) : Fragment() {

    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val formRepository = (requireActivity().application as MyApplication).formRepository
        val pageViewModel = ViewModelProvider(this, PageViewModelFactory(formRepository, yearMonth)).get(
            PageViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_page, container, false)

        val page = root.findViewById<LinearLayout>(R.id.form_page)
        val goEdit = root.findViewById<ImageButton>(R.id.go_edit_button)
        val goEdit2 = root.findViewById<Button>(R.id.go_edit_button_2)
        goEdit.visibility = View.VISIBLE
        page.visibility = View.GONE

        //adapters
        val arrayCnt = 6
        val formRecyclerViewArray = ArrayList<RecyclerView>()
        val adapterArray = ArrayList<PageFormAdapter>()
        formRecyclerViewArray.add(root.findViewById(R.id.form_1_1_recyclerview))//0
        formRecyclerViewArray.add(root.findViewById(R.id.form_2_1_recyclerview))//1
        formRecyclerViewArray.add(root.findViewById(R.id.form_2_2_recyclerview))//2
        formRecyclerViewArray.add(root.findViewById(R.id.form_3_recyclerview))//3
        formRecyclerViewArray.add(root.findViewById(R.id.form_4_recyclerview))//4
        formRecyclerViewArray.add(root.findViewById(R.id.form_5_recyclerview))//5

        for(i in 0 until arrayCnt){
            adapterArray.add(PageFormAdapter(ArrayList()))
            formRecyclerViewArray[i].adapter = adapterArray[i]
            formRecyclerViewArray[i].layoutManager = LinearLayoutManager(requireContext())
            pageViewModel.formLiveDataArray[i].observe(viewLifecycleOwner, {
                if(yearMonth == FormRepository.selectedYearMonth) {
                    requireActivity().runOnUiThread {
                        adapterArray[i].updateData(it)
                        if (it.size > 0) {
                            goEdit.visibility = View.GONE
                            page.visibility = View.VISIBLE
                        }
                    }
                }
            })
        }

        // get data at the beginning
        pageViewModel.fetchData()

        // go edit page
        goEdit.setOnClickListener{
            val intent = Intent(requireActivity(), EditActivity::class.java)
            startActivity(intent)
        }
        goEdit2.setOnClickListener{
            val intent = Intent(requireActivity(), EditActivity::class.java)
            startActivity(intent)
        }

        println("${yearMonth / 12} ${yearMonth % 12 + 1}")
        return root
    }
}