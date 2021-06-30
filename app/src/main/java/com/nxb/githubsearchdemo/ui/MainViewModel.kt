package com.nxb.githubsearchdemo.ui

import androidx.lifecycle.*
import com.nxb.githubsearchdemo.AbsentLiveData
import com.nxb.githubsearchdemo.OpenForTesting
import com.nxb.githubsearchdemo.TestUtilss
import com.nxb.githubsearchdemo.data.responses.GithubResponse
import com.nxb.githubsearchdemo.other.Event
import com.nxb.githubsearchdemo.other.Resource
import com.nxb.githubsearchdemo.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
@author Salman Aziz
created on 6/22/21
 **/

@HiltViewModel
@OpenForTesting
class MainViewModel @Inject constructor(val apiRepository: ApiRepository) : ViewModel() {

    private val _githubResponse = MutableLiveData<Event<Resource<GithubResponse>>>()
    private val _query = MutableLiveData<String>()

    private val _loadMore = MutableLiveData<Boolean>()

    var githubResponse: LiveData<Event<Resource<GithubResponse>>> = _query.switchMap {

        if (it.isEmpty()) {
            AbsentLiveData.create()
        } else {
            searchForRepo(it)
            _githubResponse
        }
    }


    private var page: Int = 1


    fun setLoadMore(input: Boolean) {
        _loadMore.value = input
    }

    fun setQuery(input: String) {
        reset()
        _query.value = input
    }

    private fun reset() {
        page = 1
    }

    fun searchNextPage() {
        _query.value?.let {
            if (it.isNotEmpty())
                searchForRepo(it)
        }

    }

    private fun searchForRepo(query: String) {

        _loadMore.value?.let {
            if (!it) {
                return@searchForRepo
            }
        }

        if (query.isEmpty()) {
            _githubResponse.value = (Event(Resource.error("Query Cannot be empty", null)))
            return
        }


        if (_githubResponse.value?.peakContent() == Resource.loading(null)) {
            return
        }


        _githubResponse.value = Event(Resource.loading(null))
        viewModelScope.launch {

            val resp = apiRepository.getSearches(query, page = (page++).toString())

            _githubResponse.value = Event(resp)


        }
    }


}