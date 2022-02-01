package com.codem.squaro.api

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

/**
 * Created by Doumith.A on 8/3/21.
 */
data class Data<T>(
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<T>>,
    // represents the network request status to show to the user
    val networkState: LiveData<NetworkState>)
