package com.ilnur.mobileowner.interfaces.views.report

import android.widget.TextView
import com.ilnur.mobileowner.domain.models.request.ReportCarsList
import com.ilnur.mobileowner.domain.models.response.DefaultRequest

interface ReportAddFragmentView {
    fun initVars()
    fun clickListener()
    fun showToast(text: String)
    fun showReportTypeAlertDialog()
    fun showReportCarsAlertDialog()
    fun openDataPickerFragment(textView: TextView)
    fun openTimePickerFragment(textView: TextView)
    fun populateReportTypeAdapter(typeList: List<DefaultRequest>)
    fun populateReportCarsAdapter(carsList: List<ReportCarsList>)
    fun selectedReportType(reportType: String)
    fun selectedReportCars(reportCars: String)
    fun showReportSuccessToast()
    fun clearTextView()
}