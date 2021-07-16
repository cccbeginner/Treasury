package com.example.treasury

import android.app.Application
import com.example.treasury.formDatabase.FormDatabase
import com.example.treasury.formDatabase.FormRepository
import java.util.*
import kotlin.properties.Delegates

class MyApplication : Application() {
    private val formDao by lazy { FormDatabase.getInstance(this).getDao() }
    val formRepository by lazy { FormRepository(formDao) }

    override fun onCreate() {
        super.onCreate()
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        current = year * 12 + month
        end = current + after
    }

    companion object{
        const val start = 2010*12
        const val after = 0
        var current by Delegates.notNull<Int>()
        var end by Delegates.notNull<Int>()
    }
}