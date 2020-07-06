package com.ilnur.mobileowner.data.database

import io.reactivex.Single
import com.ilnur.mobileowner.domain.models.request.CarsModel
import com.ilnur.mobileowner.interfaces.repositories.LocalDbRepositoryInterface

class LocalDbRepository(private val roomDatabase: AppRoomDatabase): LocalDbRepositoryInterface {

    companion object {
        private var dbRepository: LocalDbRepository? = null

        fun getInstance(roomDatabase: AppRoomDatabase): LocalDbRepository {
            if(dbRepository == null)
                dbRepository = LocalDbRepository(roomDatabase)
            return dbRepository!!
        }
    }

    override fun deleteVehicles() {
        //TODO Delete Vehicles
    }

    override fun insertCars(car: CarsModel) {
        roomDatabase.carsDao().insert(car)
    }

    override fun getCars(): Single<List<CarsModel>> {
        return roomDatabase.carsDao().getCars()
    }
}