package com.testtask.traineeship.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.testtask.traineeship.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ContactListFragment())
                .commitNow()
        }
    }
}