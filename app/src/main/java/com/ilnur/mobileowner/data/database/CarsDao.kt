package com.ilnur.mobileowner.data.database

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single
import com.ilnur.mobileowner.domain.models.request.CarsModel

@Dao
interface CarsDao: BaseDao<CarsModel> {
    @Query(value = "SELECT * FROM cars")
    fun getCars(): Single<List<CarsModel>>
}