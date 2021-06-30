package com.nxb.githubsearchdemo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nxb.githubsearchdemo.BuildConfig
import com.nxb.githubsearchdemo.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: CustomFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory=fragmentFactory

        setContentView(R.layout.activity_main)
     }
}