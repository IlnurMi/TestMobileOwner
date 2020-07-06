package com.ilnur.mobileowner.interfaces.repositories

import io.reactivex.Single
import com.ilnur.mobileowner.domain.models.request.CarsModel

interface LocalDbRepositoryInterface {

    fun deleteVehicles()
    fun insertCars(car: CarsModel)
    fun getCars(): Single<List<CarsModel>>

}