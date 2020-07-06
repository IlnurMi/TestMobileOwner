package com.ilnur.mobileowner.presentation.presenters.report

import io.reactivex.android.schedulers.AndroidSchedulers
import com.ilnur.mobileowner.domain.interactors.report.ReportAddInteractor
import com.ilnur.mobileowner.domain.models.MainResponceArray
import com.ilnur.mobileowner.domain.models.MainResponceObject
import com.ilnur.mobileowner.domain.models.request.DefaultObjectRequest
import com.ilnur.mobileowner.domain.models.request.ReportCarsList
import com.ilnur.mobileowner.domain.models.response.DefaultRequest
import com.ilnur.mobileowner.interfaces.views.report.ReportAddFragmentView
import com.ilnur.mobileowner.presentation.PresentationHelper

class ReportAddPresenter(private val interactor: ReportAddInteractor) {
    private var view: ReportAddFragmentView? = null
    private var selectedReportTypeId: String = ""
    private var selectedReportTypeName: String = ""
    private var selectedReportCarsId: String = ""
    private var selectedReportCarsName: String = ""

    fun setView(view: ReportAddFragmentView){
        this.view = view
    }

    fun getCarList(){
        interactor
            .getReportCars()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({result -> responseCarsSuccess(result)},{error -> responseCarsError(error)})
    }

    fun getReportType(){
        interactor
            .getReportType()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({result -> responseSuccess(result)},{ error -> responseError(error)})

    }

    fun postReport(dateStart: String, dateEnd: String){
        val reportOrderRequest = interactor.prepareReportOrderRequest(selectedReportTypeId, selectedReportCarsId, dateStart, dateEnd)

        interactor
            .sendReport(reportOrderRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({result -> responseGetReportSuccess(result)},{error -> responseGetReportError(error)})
    }

    private fun responseSuccess(result: MainResponceArray<DefaultRequest>){
        view?.populateReportTypeAdapter(result.list)
    }

    private fun responseError(error: Throwable){
    }

    private fun responseCarsSuccess(result: MainResponceArray<ReportCarsList>){
        view?.populateReportCarsAdapter(result.list)
    }

    private fun responseCarsError(error: Throwable){
    }

    private fun responseGetReportSuccess(result: MainResponceObject<DefaultObjectRequest>){
        view?.showReportSuccessToast()
    }

    private fun responseGetReportError(error: Throwable){
    }

    // Плохая реализация
    fun reportTypeAlertClick(defaultRequest: DefaultRequest){
        selectedReportTypeId = defaultRequest.id
        selectedReportTypeName = defaultRequest.name
        view?.selectedReportType(defaultRequest.name)
    }

    fun reportCarsAlertClick(reportCarsList: ReportCarsList){
        selectedReportCarsId = reportCarsList.id
        selectedReportCarsName = reportCarsList.name
        view?.selectedReportCars(reportCarsList.name)
    }

    fun comparisonDate(dateStart: String, dateEnd: String): Boolean{
        return PresentationHelper.getInstance().comparisonDate(dateStart, dateEnd)
    }
}