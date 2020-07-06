package com.ilnur.mobileowner.data.repositories.report

import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Call
import com.ilnur.mobileowner.data.network.RestService
import com.ilnur.mobileowner.data.preferences.PreferenceRepository
import com.ilnur.mobileowner.domain.models.MainResponceArray
import com.ilnur.mobileowner.domain.models.MainResponceObject
import com.ilnur.mobileowner.domain.models.request.DefaultObjectRequest
import com.ilnur.mobileowner.domain.models.request.ReportCarsList
import com.ilnur.mobileowner.domain.models.request.ReportOrderRequest
import com.ilnur.mobileowner.domain.models.request.ReportRequest
import com.ilnur.mobileowner.domain.models.response.DefaultRequest
import ru.itis.kalimullinri.itisowner.interfaces.repositories.report.ReportRepositoryInterface

class ReportRepository(var restService: RestService, private var restServiceTest: RestService): ReportRepositoryInterface {

    override fun getReportCars(): Flowable<MainResponceArray<ReportCarsList>> {
        return restService.getReportCars(PreferenceRepository.INSTANCE.getToken())
    }

    override fun getReportType(): Flowable<MainResponceArray<DefaultRequest>> {
        return restService.getReportType(PreferenceRepository.INSTANCE.getToken())
    }

    override fun sendReport(reportOrderRequest: ReportOrderRequest): Flowable<MainResponceObject<DefaultObjectRequest>> {
        return restService.getReport(PreferenceRepository.INSTANCE.getToken(), reportOrderRequest)
    }

    override fun getReportFormat(): Flowable<MainResponceArray<DefaultRequest>> {
        return  restService.getReportFormat(PreferenceRepository.INSTANCE.getToken())
    }

    override fun getReportList(): Flowable<MainResponceArray<ReportRequest>> {
        return restService.getReportList(PreferenceRepository.INSTANCE.getToken())
    }

    override fun downloadReport(reportId: Int, type: Int): Call<ResponseBody> {
       return restService.downloadReport(PreferenceRepository.INSTANCE.getToken(), reportId, type)
    }

    override fun testDownload(): Call<ResponseBody> {
        return  restServiceTest.downloadTestReport()
    }

}