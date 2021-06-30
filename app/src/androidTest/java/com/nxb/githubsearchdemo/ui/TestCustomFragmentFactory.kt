package com.nxb.githubsearchdemo.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.nxb.githubsearchdemo.adapters.ReposAdapter
import com.nxb.githubsearchdemo.repository.ApiRepository
import javax.inject.Inject

/**
@author Salman Aziz
created on 6/22/21
 **/

class TestCustomFragmentFactory @Inject constructor(val apiRepository: ApiRepository, val reposAdapter: ReposAdapter) :FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            MainFragment::class.java.name -> MainFragment(reposAdapter=reposAdapter )
            else ->super.instantiate(classLoader, className)
        }
    }
}