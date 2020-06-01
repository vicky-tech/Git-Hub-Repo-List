package com.example.githubrepo.data.remote

import com.example.githubrepo.data.remote.model.RepoItem
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("octokit/repos")
    fun getRepoListAsync(@Query("page") pageNo: Int, @Query("per_page") pageSize: Int): Deferred<List<RepoItem>>
}