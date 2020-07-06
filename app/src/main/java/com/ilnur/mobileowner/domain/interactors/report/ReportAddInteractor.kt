package com.ilnur.mobileowner.domain.interactors.report

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import com.ilnur.mobileowner.domain.models.MainResponceArray
import com.ilnur.mobileowner.domain.models.MainResponceObject
import com.ilnur.mobileowner.domain.models.request.DefaultObjectRequest
import com.ilnur.mobileowner.domain.models.request.ReportCarsList
import com.ilnur.mobileowner.domain.models.request.ReportOrderRequest
import com.ilnur.mobileowner.domain.models.response.DefaultRequest
import com.ilnur.mobileowner.interfaces.repositories.report.ReportAddRepositoryInterface

class ReportAddInteractor(private val repository: ReportAddRepositoryInterface) {
    fun getReportType(): Flowable<MainResponceArray<DefaultRequest>> {
        return repository
            .getReportType()
            .subscribeOn(Schedulers.io())
    }

    fun getReportCars(): Flowable<MainResponceArray<ReportCarsList>> {
        return  repository
            .getReportCars()
            .subscribeOn(Schedulers.io())
    }

    fun sendReport(report: ReportOrderRequest): Flowable<MainResponceObject<DefaultObjectRequest>>{
        return repository
            .sendReport(report)
            .subscribeOn(Schedulers.io())
    }

    fun prepareReportOrderRequest(reportType: String, carId: String, dateStart: String, dateEnd: String): ReportOrderRequest {
        val reportOrderRequest = ReportOrderRequest()
        reportOrderRequest.apply {
            CarId = listOf(carId.toInt())
            Report = reportType.toInt()
            DateBegin = changeDate(dateStart)
            DateEnd = changeDate(dateEnd)
        }
        return  reportOrderRequest
    }

    private fun changeDate(date: String): String{
        val dateBase = date.substringBefore(' ')
        val time = date.substringAfter(' ')
        val day = dateBase.substringBefore('.')
        val date1 = dateBase.substringAfter('.')
        val month = date1.substringBefore('.')
        val year = date1.substringAfter('.')
        return  "${year}-${month}-${day}T$time"
    }
}