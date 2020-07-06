package com.ilnur.mobileowner.data.database


import androidx.room.*

@Dao
interface BaseDao<in T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: T)

    @Delete
    fun delete(type: T)

    @Update
    fun update(type: T)
}