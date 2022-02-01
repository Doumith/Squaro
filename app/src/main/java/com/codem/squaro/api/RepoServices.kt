package com.codem.squaro.api

import com.codem.squaro.Constants
import com.codem.squaro.data.entities.RepoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Doumith.A on 8/3/21.
 */
interface RepoServices {

    @GET(Constants.REPO_URL)
    suspend fun getRepositoryList(
        @Query("page") page: Int? = null,
    ): Response<List<RepoModel>>

}