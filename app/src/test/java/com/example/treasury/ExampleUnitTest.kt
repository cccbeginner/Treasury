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

        var a = mutableMapOf<Int, Int>()
        a[0] = 1
        a[1] = 2
        a[2] = 3
        for (e in a){
            e.setValue(e.key)
        }

        assertEquals(mapOf(0 to 0, 1 to 1, 2 to 2), a.toMap())
    }
}