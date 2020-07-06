package com.ilnur.mobileowner.interfaces.views.report

interface ReportAdapterListener {
    fun download(reportId: Int, filename: String)
    fun checkPermission(): Boolean
}