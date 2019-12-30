package com.rashwan.SuperCase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


data class Movie(val title: String, val year: Int)


class test1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_1)

    }
}



