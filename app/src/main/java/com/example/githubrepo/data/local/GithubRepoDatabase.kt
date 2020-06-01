package com.example.githubrepo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubrepo.data.remote.model.RepoItem


// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [RepoItem::class], version = 1, exportSchema = false)
public abstract class GithubRepoDatabase : RoomDatabase() {

    abstract fun repoDao(): GithubRepoDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: GithubRepoDatabase? = null

        fun getDatabase(context: Context): GithubRepoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                if (INSTANCE != null) {
                    return INSTANCE!!
                }
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GithubRepoDatabase::class.java,
                    "repo_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}