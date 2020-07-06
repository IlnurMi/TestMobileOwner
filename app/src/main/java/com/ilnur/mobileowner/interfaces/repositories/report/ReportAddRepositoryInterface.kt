package com.ilnur.mobileowner.interfaces.repositories.report

import io.reactivex.Flowable
import com.ilnur.mobileowner.domain.models.MainResponceArray
import com.ilnur.mobileowner.domain.models.MainResponceObject
import com.ilnur.mobileowner.domain.models.request.DefaultObjectRequest
import com.ilnur.mobileowner.domain.models.request.ReportCarsList
import com.ilnur.mobileowner.domain.models.request.ReportOrderRequest
import com.ilnur.mobileowner.domain.models.response.DefaultRequest

interface ReportAddRepositoryInterface {
    fun getReportType(): Flowable<MainResponceArray<DefaultRequest>>
    fun getReportCars(): Flowable<MainResponceArray<ReportCarsList>>
    fun sendReport(reportOrderRequest: ReportOrderRequest): Flowable<MainResponceObject<DefaultObjectRequest>>
}