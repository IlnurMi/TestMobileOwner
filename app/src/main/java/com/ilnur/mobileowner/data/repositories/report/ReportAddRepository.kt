package com.ilnur.mobileowner.data.repositories.report

import io.reactivex.Flowable
import com.ilnur.mobileowner.data.network.RestService
import com.ilnur.mobileowner.data.preferences.PreferenceRepository
import com.ilnur.mobileowner.domain.models.MainResponceArray
import com.ilnur.mobileowner.domain.models.MainResponceObject
import com.ilnur.mobileowner.domain.models.request.DefaultObjectRequest
import com.ilnur.mobileowner.domain.models.request.ReportCarsList
import com.ilnur.mobileowner.domain.models.request.ReportOrderRequest
import com.ilnur.mobileowner.domain.models.response.DefaultRequest
import com.ilnur.mobileowner.interfaces.repositories.report.ReportAddRepositoryInterface

class ReportAddRepository(var restService: RestService, var restServiceTest: RestService):
    ReportAddRepositoryInterface {
    override fun getReportCars(): Flowable<MainResponceArray<ReportCarsList>> {
        return restService.getReportCars(PreferenceRepository.INSTANCE.getToken())
    }

    override fun getReportType(): Flowable<MainResponceArray<DefaultRequest>> {
        return restService.getReportType(PreferenceRepository.INSTANCE.getToken())
    }

    override fun sendReport(reportOrderRequest: ReportOrderRequest): Flowable<MainResponceObject<DefaultObjectRequest>> {
        return restService.getReport(PreferenceRepository.INSTANCE.getToken(), reportOrderRequest)
    }
}