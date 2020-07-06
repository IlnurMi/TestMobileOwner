package com.ilnur.mobileowner.domain.interactors.report

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import com.ilnur.mobileowner.domain.models.MainResponceArray
import com.ilnur.mobileowner.domain.models.request.ReportRequest
import com.ilnur.mobileowner.domain.models.response.DefaultRequest
import ru.itis.kalimullinri.itisowner.interfaces.repositories.report.ReportRepositoryInterface

class ReportInteractor(private val repository: ReportRepositoryInterface) {
    fun getReportFormat(): Flowable<MainResponceArray<DefaultRequest>>{
        return repository
            .getReportFormat()
            .subscribeOn(Schedulers.io())
    }

    fun getReportList(): Flowable<MainResponceArray<ReportRequest>>{
        return repository
            .getReportList()
            .subscribeOn(Schedulers.io())
    }

    fun downloadReport(reportId: Int, type: Int): Call<ResponseBody> {
        return repository
            .downloadReport(reportId, type)
    }
}