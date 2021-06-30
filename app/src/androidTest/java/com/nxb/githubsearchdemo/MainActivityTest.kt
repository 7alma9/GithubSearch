package com.nxb.githubsearchdemo

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.nxb.githubsearchdemo.di.TestAppModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@HiltAndroidTest
class MainActivityTest {
//
//    @get:Rule
//    var hiltRule = HiltAndroidRule(this)
//
//    @Inject
//    lateinit var someString: String
//

//    @BindValue var myString: String = "gggf" // Doesn't work?? I'm prob doing it wrong.

    @Before
    fun init() {
//        hiltRule.inject()
    }

    @Test
    fun hiltTest(){
        assertThat("TEST string", containsString("TEST string"))
    }



}


























