package com.example.githubrepo.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.githubrepo.data.remote.model.RepoItem
import com.example.githubrepo.data.repository.GithubRepoRepository
import com.example.githubrepo.data.repository.Resource
import com.example.githubrepo.data.repository.Status
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class GithubRepoViewModel(context: Application) : AndroidViewModel(context) {

    private val repository :GithubRepoRepository = GithubRepoRepository(context)
    private val _repoList  =  MediatorLiveData<List<RepoItem>>()
    val repoList : LiveData<List<RepoItem>>
        get() = _repoList

    private val _status =  MutableLiveData<Status>()
    val status : LiveData<Status>
        get() = _status

    val handler = CoroutineExceptionHandler { _, _ ->
        _status.value = Status.ERROR
    }


    init {
        viewModelScope.launch(handler) {
         val result =  repository.getRepoList()
            postResult(result)
        }

    }

    fun loadMore() {
        // already request in process
        if(status.value == Status.LOADING)
            return
        viewModelScope.launch(handler) {
            repository.fetchMoreDataFromServer(repoList.value?.size ?: 0)
        }
    }

    private fun postResult(result: LiveData<Resource<List<RepoItem>>>) {
        _repoList.addSource(result){
            when(it.status){
                Status.LOADING -> _status.postValue(Status.LOADING)
                Status.ERROR -> _status.postValue(Status.ERROR)
                Status.SUCCESS -> {
                    _repoList.postValue(it.data)
                    _status.postValue(Status.SUCCESS)
                }
            }
        }
    }
}