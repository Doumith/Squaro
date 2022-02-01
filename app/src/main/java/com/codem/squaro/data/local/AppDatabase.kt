package com.codem.squaro.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codem.squaro.data.entities.RepoModel


/**
 * Created by Doumith.A on 8/3/21.
 */
@Database(entities = [RepoModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao

}