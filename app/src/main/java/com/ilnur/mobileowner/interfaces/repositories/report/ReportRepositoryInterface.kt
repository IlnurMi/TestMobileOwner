package ru.itis.kalimullinri.itisowner.interfaces.repositories.report

import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Call
import com.ilnur.mobileowner.domain.models.MainResponceArray
import com.ilnur.mobileowner.domain.models.MainResponceObject
import com.ilnur.mobileowner.domain.models.request.DefaultObjectRequest
import com.ilnur.mobileowner.domain.models.request.ReportCarsList
import com.ilnur.mobileowner.domain.models.request.ReportOrderRequest
import com.ilnur.mobileowner.domain.models.request.ReportRequest
import com.ilnur.mobileowner.domain.models.response.DefaultRequest

interface ReportRepositoryInterface {
    fun getReportType(): Flowable<MainResponceArray<DefaultRequest>>
    fun getReportCars(): Flowable<MainResponceArray<ReportCarsList>>
    fun sendReport(reportOrderRequest: ReportOrderRequest): Flowable<MainResponceObject<DefaultObjectRequest>>
    fun getReportFormat(): Flowable<MainResponceArray<DefaultRequest>>
    fun getReportList(): Flowable<MainResponceArray<ReportRequest>>
    fun downloadReport(reportId: Int, type: Int): Call<ResponseBody>
    fun testDownload(): Call<ResponseBody>
}