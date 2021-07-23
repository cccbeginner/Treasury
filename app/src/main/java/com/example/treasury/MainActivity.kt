package com.example.treasury

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.treasury.page.PageFragment

class MainActivity : AppCompatActivity() {

    var selectedYear = -1
    var selectedMonth = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // decide how many pages should I create (?)
        val startYear = MyApplication.start / 12

        // current year and month
        val currentYearMonth = MyApplication.current
        selectedYear = currentYearMonth / 12
        selectedMonth = currentYearMonth % 12
        refreshPage()

        // set year spinners
        val spinnerYear = findViewById<Spinner>(R.id.spinner_year)
        spinnerYear.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.year_array,
            R.layout.spinner_item)
        spinnerYear.setSelection(currentYearMonth / 12 - startYear)
        spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedYear = startYear + position
                refreshPage()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        // set month spinners
        val spinnerMonth = findViewById<Spinner>(R.id.spinner_month)
        spinnerMonth.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.month_array,
            R.layout.spinner_item)
        spinnerMonth.setSelection(currentYearMonth % 12)
        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedMonth = position
                refreshPage()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun refreshPage(){
        val yearMonth = selectedYear * 12 + selectedMonth
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_page, PageFragment(yearMonth))
            .commit()
    }
}