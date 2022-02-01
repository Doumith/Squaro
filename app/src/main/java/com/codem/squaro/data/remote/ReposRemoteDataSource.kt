package com.codem.squaro.data.remote

import com.codem.squaro.api.BaseDataSource
import com.codem.squaro.api.RepoServices
import com.codem.squaro.api.Result
import com.codem.squaro.data.entities.RepoModel
import javax.inject.Inject

class ReposRemoteDataSource @Inject constructor(private val repoServices: RepoServices) :
    BaseDataSource() {

    suspend fun getRepos(page: Int): Result<List<RepoModel>> {
        return getResult { repoServices.getRepositoryList(page) }
    }
}
