package com.ilnur.mobileowner.interfaces.views.report

import com.ilnur.mobileowner.domain.models.request.ReportCarsList

interface ReportCarAdapterListener {
    fun selectCar(car: ReportCarsList)
    fun dialogDismiss()
}