package com.example.githubrepo.data.repository

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import java.util.*

abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result = MediatorLiveData<Resource<ResultType>>()
    private val supervisorJob = SupervisorJob()

    init {
        // not limiting with coroutine scope because we are fetching latest data from server, need not to cancel api call if user close the screen
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    // ---

    private  fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            setValue(Resource.success(newData))
        }

        CoroutineScope(Dispatchers.IO).launch {
            // Timber.i("apiResponse")
            when(val apiResponse = createCallAsync().await()) {
                is Throwable -> {
                    setValue(Resource.error(apiResponse,null))
                }
                else -> {
                    withContext(Dispatchers.Default) {
                        processAndSaveResponse(apiResponse)
                    }

                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        Log.d(NetworkBoundResource::class.java.name, "Resource: " + newValue)
        if (result.value != newValue) result.postValue(newValue)
    }

    @WorkerThread
    protected abstract fun processAndSaveResponse(response: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @WorkerThread
    protected abstract  fun loadFromDb(): LiveData<ResultType>

    @WorkerThread
    protected abstract fun createCallAsync(): Deferred<RequestType>
}