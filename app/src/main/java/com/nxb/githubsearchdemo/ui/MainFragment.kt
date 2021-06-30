package com.nxb.githubsearchdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nxb.githubsearchdemo.R
import com.nxb.githubsearchdemo.adapters.ReposAdapter
import com.nxb.githubsearchdemo.data.responses.GithubResponse
import com.nxb.githubsearchdemo.databinding.FragmentMainBinding
import com.nxb.githubsearchdemo.other.Event
import com.nxb.githubsearchdemo.other.Resource
import com.nxb.githubsearchdemo.other.SPAN_COUNT
import com.nxb.githubsearchdemo.other.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
@author Salman Aziz
created on 6/22/21
 **/

@AndroidEntryPoint
class MainFragment  @Inject constructor(   val reposAdapter: ReposAdapter) : Fragment() {

     private lateinit var binding: FragmentMainBinding

    private val mainViewModel: MainViewModel by viewModels()

     override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecycler()
        startObserving()


        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                query?.let {

                    mainViewModel?.setQuery(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }

      fun setupRecycler() {
        binding.rvRepos.apply {
            adapter = reposAdapter
            layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == reposAdapter.itemCount - 1) {
                        mainViewModel?.searchNextPage()
                    }
                }
            })
        }


    }


      fun setRepoItems(response: GithubResponse){
        reposAdapter.repoItems = response.items
        mainViewModel?.setLoadMore(response.items.isNotEmpty())
    }
      fun startObserving() {
        mainViewModel?.githubResponse?.observe(viewLifecycleOwner, { event ->

            when (event.peakContent().status) {
                Status.SUCCESS -> {
                    event.getContentIfNotHandeled()?.data?.let { response ->
                        setRepoItems(response)
                    }
                    showHide(binding.progress,false)


                }
                Status.ERROR -> {
                    showHide(binding.progress,false)

                }
                Status.LOADING -> {
                    showHide(binding.progress,true)
                }

            }


        })

    }
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
}