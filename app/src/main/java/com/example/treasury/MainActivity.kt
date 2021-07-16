package com.example.treasury

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.treasury.formDatabase.FormRepository
import com.example.treasury.page.PageFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // decide how many pages should I create (?)
        val start = MyApplication.start
        val end = MyApplication.end

        // current year and month
        val currentYearMonth = MyApplication.current
        FormRepository.selectedYearMonth = currentYearMonth

        // resource to create pages
        val fragments = ArrayList<Fragment>()
        val titles = ArrayList<String>()
        for (yearMonth in start..end){
            val newYear = yearMonth / 12
            val newMonth = yearMonth % 12 + 1
            titles.add("$newYear 年 $newMonth 月")
            fragments.add(newFragment(yearMonth))
        }

        // ui -- viewPager2
        val pageAdapter = PageAdapter(this.supportFragmentManager, lifecycle, fragments)
        val viewPager2: ViewPager2 = findViewById(R.id.pages)
        viewPager2.adapter = pageAdapter
        viewPager2.currentItem = currentYearMonth - start
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                FormRepository.selectedYearMonth = position + start
                println("on page selected $position")
            }
        })

        // ui -- tabLayout
        val tabLayout: TabLayout = findViewById(R.id.tabs)
        tabLayout.setScrollPosition(currentYearMonth - start, 0f, true)
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    class PageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val fragments: ArrayList<Fragment>) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    private fun newFragment(yearMonth: Int): Fragment {
        return PageFragment(yearMonth)
    }
}