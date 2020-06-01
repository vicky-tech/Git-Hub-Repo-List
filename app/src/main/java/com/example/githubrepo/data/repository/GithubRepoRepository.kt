package com.example.githubrepo.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.githubrepo.util.PAGE_SIZE
import com.example.githubrepo.data.local.GithubRepoDatabase
import com.example.githubrepo.data.local.sharedpref.AppPref
import com.example.githubrepo.data.remote.RestClient
import com.example.githubrepo.data.remote.model.RepoItem
import com.example.githubrepo.util.START_PAGE_NO
import com.example.githubrepo.util.isNetworkAvailable
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GithubRepoRepository(val context: Context) {

    val database = GithubRepoDatabase.getDatabase(context)
    val currentPage =  AppPref.getPageNo(context = context)

    fun getRepoList() : LiveData<Resource<List<RepoItem>>> {

        return object : NetworkBoundResource<List<RepoItem>,List<RepoItem>>() {

            override fun processAndSaveResponse(response: List<RepoItem>) {
                database.repoDao().insertAll(response)
            }

            override fun shouldFetch(data: List<RepoItem>?): Boolean {
                // added check if there is no more data available like if we know last page No
                return data.isNullOrEmpty() && context.isNetworkAvailable()
            }

            override  fun loadFromDb(): LiveData<List<RepoItem>> {
                return database.repoDao().getRepoList()
            }

            override fun createCallAsync(): Deferred<List<RepoItem>> {
                return RestClient.getApiService().getRepoListAsync(
                    START_PAGE_NO,
                    PAGE_SIZE
                )
            }

        }.asLiveData()
    }
    suspend fun fetchMoreDataFromServer(currentSize : Int){
        withContext(Dispatchers.IO) {
            val nexPage = currentSize/ PAGE_SIZE + 1
            if(currentSize % PAGE_SIZE == 0 && context.isNetworkAvailable()) {
              val result =  RestClient.getApiService().getRepoListAsync(
                    nexPage,
                    PAGE_SIZE
                ).await()
                withContext(Dispatchers.Default){
                    database.repoDao().insertAll(result)
                }

            }
        }
    }

}