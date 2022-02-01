package com.codem.squaro.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


/**
 * Created by Doumith.A on 8/3/21.
 */
@Entity
data class RepoModel(
    @PrimaryKey
    var id: Int,
    var name: String? = null,
    @SerializedName("stargazers_count")
    var starsCount: Int? = null,
    var isBookMarked: Boolean = false
)
