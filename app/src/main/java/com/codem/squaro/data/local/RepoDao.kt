package com.codem.squaro.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.paging.DataSource
import androidx.room.*
import com.codem.squaro.data.entities.RepoModel

/**
 * Created by Doumith.A on 8/3/21.
 */
@Dao
interface RepoDao {

    @Query("SELECT * FROM RepoModel WHERE id = :id")
    fun getRepository(id: Int): LiveData<RepoModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<RepoModel>)

    @Query("SELECT * FROM RepoModel")
    fun getAllRepositories(): DataSource.Factory<Int, RepoModel>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(repo: RepoModel): Int

    @Query("SELECT * FROM RepoModel WHERE isBookMarked=1")
    suspend fun getBookmarks(): List<RepoModel>

    fun getRepoUntilChanged(id: Int) =
        getRepository(id).distinctUntilChanged()
}