package com.codem.squaro.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder

import com.codem.squaro.api.Data
import com.codem.squaro.api.NetworkState
import com.codem.squaro.data.entities.RepoModel
import com.codem.squaro.data.local.RepoDao
import com.codem.squaro.data.remote.ReposRemoteDataSource
import com.codem.squaro.data.remote.RepoDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RepoRepository @Inject constructor(
    private val repoDao: RepoDao,
    private val remoteDataSource: ReposRemoteDataSource
) {

    fun observePagedRepos(connectivityAvailable: Boolean, coroutineScope: CoroutineScope)
            : Data<RepoModel> {

        return if (connectivityAvailable)
            observeRemotePagedRepos(coroutineScope)
        else observeLocalPagedRepos()
    }

    private fun observeLocalPagedRepos(): Data<RepoModel> {

        val dataSourceFactory = repoDao.getAllRepositories()

        val createLD = MutableLiveData<NetworkState>()
        createLD.postValue(NetworkState.LOADED)

        return Data(
            LivePagedListBuilder(
                dataSourceFactory,
                RepoDataSourceFactory.pagedListConfig()
            ).build(), createLD
        )
    }

    private fun observeRemotePagedRepos(ioCoroutineScope: CoroutineScope): Data<RepoModel> {
        val dataSourceFactory = RepoDataSourceFactory(
            remoteDataSource,
            repoDao, ioCoroutineScope
        )

        val networkState = Transformations.switchMap(dataSourceFactory.liveData) {
            it.networkState
        }
        return Data(
            LivePagedListBuilder(
                dataSourceFactory,
                RepoDataSourceFactory.pagedListConfig()
            ).build(), networkState
        )
    }

    suspend fun updateRepo(repo: RepoModel) {
        withContext(IO) {
            repoDao.update(repo)
        }
    }

    fun getRepoUntilChanged(repo: RepoModel): LiveData<RepoModel> {
        return repoDao.getRepoUntilChanged(repo.id)
    }

}
