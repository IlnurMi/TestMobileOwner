package com.ilnur.mobileowner.interfaces.views.report

import com.ilnur.mobileowner.domain.models.request.ReportCarsList
import com.ilnur.mobileowner.domain.models.request.ReportRequest
import com.ilnur.mobileowner.domain.models.response.DefaultRequest

interface ReportView {
    fun initVars()
    fun clickListeners()
    fun populateReportTypeAdapter(typeList: List<DefaultRequest>)
    fun populateReportCarsAdapter(carsList: List<ReportCarsList>)
    fun populateReportFormatAdapter(reportFormatList: List<DefaultRequest>)
    fun populateReportListAdapter(reportList: List<ReportRequest>)
    fun showReportFormatAlertDialog()
    fun selectedReportType(reportType: String)
    fun selectedReportCars(reportCars: String)
    fun selectedReportFormat(reportFormat: String)
    fun selectedReport(report: String)
    fun showReportSuccessToast()
    fun createNotification(filename: String)
    fun showToastStartDownloadReport()
    fun requestPermissions(): Boolean
    fun showToast(text: String)
    fun showProgressDialog(show: Boolean)
}