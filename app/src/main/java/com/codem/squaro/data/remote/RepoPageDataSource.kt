package com.codem.squaro.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.codem.squaro.Constants.Companion.DEFAULT_PAGE_INDEX
import com.codem.squaro.api.NetworkState
import com.codem.squaro.data.entities.RepoModel
import com.codem.squaro.data.local.RepoDao
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.codem.squaro.api.Result

class RepoPageDataSource @Inject constructor(
    private val remoteDataSource: ReposRemoteDataSource,
    private val repoDao: RepoDao,
    private val coroutineScope: CoroutineScope
) : PageKeyedDataSource<Int, RepoModel>() {

    val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, RepoModel>
    ) {
        networkState.postValue(NetworkState.LOADING)
        fetchData(DEFAULT_PAGE_INDEX) {
            callback.onResult(it, null, DEFAULT_PAGE_INDEX + 1)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, RepoModel>) {
        networkState.postValue(NetworkState.LOADING)
        val page = params.key
        fetchData(page) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, RepoModel>) {
        val page = params.key
        fetchData(page) {
            callback.onResult(it, page - 1)
        }
    }

    private fun fetchData(page: Int, callback: (List<RepoModel>) -> Unit) {
        coroutineScope.launch(getJobErrorHandler()) {
            val response = remoteDataSource.getRepos(page)
            if (response.status == Result.Status.SUCCESS) {
                val results = response.data ?: emptyList()
                val bookmarks = repoDao.getBookmarks().map { it.id }
                results.filter { bookmarks.contains(it.id) }.forEach { it.isBookMarked = true }
                repoDao.insertAll(results)
                callback(results)
                networkState.postValue(NetworkState.LOADED)
            } else if (response.status == Result.Status.ERROR) {
                networkState.postValue(NetworkState.error(response.message ?: "Unknown error"))
                postError(response.message ?: "Unknown error")
            }
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        postError(e.message ?: e.toString())
    }

    private fun postError(message: String) {
        Log.e("error", "An error happened: $message")
    }
}
