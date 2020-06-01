package com.example.githubrepo.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubrepo.data.remote.model.RepoItem

@Dao
interface GithubRepoDao {
    @Query("Select * from  RepoItem")
     fun getRepoList() : LiveData<List<RepoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data : List<RepoItem>)
}