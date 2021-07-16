package com.example.treasury

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun correct() {

        var array = ArrayList<ArrayList<Int>>()
        array.add(arrayListOf())
        array.add(arrayListOf())
        array.add(arrayListOf())

        assertEquals(3, array.size)
    }
}