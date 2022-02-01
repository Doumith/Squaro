package com.codem.squaro.data.remote

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.codem.squaro.data.entities.RepoModel
import com.codem.squaro.data.local.RepoDao
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class RepoDataSourceFactory @Inject constructor(
    private val dataSource: ReposRemoteDataSource,
    private val dao: RepoDao,
    private val scope: CoroutineScope
) : DataSource.Factory<Int, RepoModel>() {

    val liveData = MutableLiveData<RepoPageDataSource>()

    override fun create(): DataSource<Int, RepoModel> {
        val source = RepoPageDataSource(dataSource, dao, scope)
        liveData.postValue(source)
        return source
    }

    companion object {
        private const val PAGE_SIZE = 20
        fun pagedListConfig() = PagedList.Config.Builder()
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()
    }
}
