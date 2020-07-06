package com.ilnur.mobileowner.presentation.ui.report

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilnur.mobileowner.R
import kotlinx.android.synthetic.main.new_report_fragment.*
import kotlinx.android.synthetic.main.report_fragment.*
import com.ilnur.mobileowner.data.network.ApiClient
import com.ilnur.mobileowner.data.repositories.report.ReportRepository
import com.ilnur.mobileowner.domain.interactors.report.ReportInteractor
import com.ilnur.mobileowner.domain.models.request.ReportCarsList
import com.ilnur.mobileowner.domain.models.request.ReportRequest
import com.ilnur.mobileowner.domain.models.response.DefaultRequest
import com.ilnur.mobileowner.interfaces.DrawerFragmentInterface
import com.ilnur.mobileowner.interfaces.views.report.ReportView
import com.ilnur.mobileowner.presentation.presenters.report.ReportPresenter

import java.lang.ClassCastException
import com.ilnur.mobileowner.interfaces.FragmentsListener
import com.ilnur.mobileowner.interfaces.TextChangeListener
import com.ilnur.mobileowner.interfaces.views.report.ReportAdapterListener

class ReportFragment : Fragment(), ReportView, DrawerFragmentInterface, TextChangeListener,
    ReportAdapterListener {

    private lateinit var reportPresenter: ReportPresenter
    private var reportCarsAdapter: ArrayAdapter<ReportCarsList>? = null
    private var reportTypeAdapter: ArrayAdapter<DefaultRequest>? = null
    private var reportFormatAdapter: ArrayAdapter<DefaultRequest>? = null
    private lateinit var fragmentsListener: FragmentsListener
    private var reportAdapter: ReportFragmentAdapter? = null
    private var reportList: MutableList<ReportRequest>? = null

    companion object {
        internal const val WRITE_PERMISSION_REQUEST_CODE = 1
        var INSTANCE: ReportFragment? = null

        fun getInstance(): ReportFragment {
            if (INSTANCE == null)
                INSTANCE = ReportFragment()
            return INSTANCE!!
        }

        fun newInstance(): ReportFragment {
            INSTANCE = ReportFragment()
            return INSTANCE!!
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            fragmentsListener = context as FragmentsListener
        } catch (e: ClassCastException) {
            //TODO handle error
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_report_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVars()
        clickListeners()
    }

    //если этот фрагмент Видимый, то ...
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            //... переполучаем список отчётов
            if (this::reportPresenter.isInitialized) {
                reportPresenter.apply {
                    getReportList()
                    getReportFormat()
                }
            }
        }
    }

    override fun initVars() {
        fragmentsListener.showSearchView(true)
        fragmentsListener.setActionBarTitle(getString(R.string.reports))
        reportPresenter = ReportPresenter(ReportInteractor(ReportRepository(ApiClient().getTestRestClient(requireContext()), ApiClient().getTestDownloadRestClient(requireContext()))))

        reportPresenter.setView(this)
        reportPresenter.getReportList()
        reportPresenter.getReportFormat()

        reportList = ArrayList()
        reportAdapter = ReportFragmentAdapter(reportList as ArrayList<ReportRequest>, this)
        rv_reports.layoutManager = LinearLayoutManager(requireContext())
        rv_reports.adapter = reportAdapter
    }

    override fun clickListeners() {

        swipe_refresh_layout.setOnRefreshListener {
            reportPresenter.getReportList()
        }

        fb_order_report.setOnClickListener {
            fragmentsListener.replaceDeepFragment(ReportAddFragment.geInstance())
        }
    }


    override fun showToastStartDownloadReport() {
        showToast(getString(R.string.start_download_report))
    }

    override fun createNotification(filename: String) {
        fragmentsListener.createNotification(filename)
    }

    override fun populateReportTypeAdapter(typeList: List<DefaultRequest>) {
        reportTypeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.select_dialog_singlechoice, typeList)
    }

    override fun populateReportCarsAdapter(carsList: List<ReportCarsList>) {
        reportCarsAdapter =
            ArrayAdapter(requireContext(), android.R.layout.select_dialog_singlechoice, carsList)
    }

    override fun populateReportFormatAdapter(reportFormatList: List<DefaultRequest>) {
        reportFormatAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.select_dialog_singlechoice,
                reportFormatList
            )
    }

    override fun populateReportListAdapter(reportList: List<ReportRequest>) {
        reportAdapter?.addItems(reportList)
        swipe_refresh_layout.isRefreshing = false
    }

    override fun showReportFormatAlertDialog() {
        if (reportFormatAdapter != null) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(R.string.select_report)
            builder.setSingleChoiceItems(reportFormatAdapter, -1) { dialog, which ->
                val listView = (dialog as AlertDialog).listView
                val reportFormat: DefaultRequest =
                    listView.getItemAtPosition(which) as DefaultRequest
                reportPresenter.reportFormatAlertClick(reportFormat)
                dialog.dismiss()
            }.create().show()
        } else {
            showToast(getString(R.string.list_empty))
        }
    }

    override fun selectedReportType(reportType: String) {
        tv_report_type.text = reportType
    }

    override fun selectedReportCars(reportCars: String) {
        tv_cars.text = reportCars
    }

    override fun selectedReportFormat(reportFormat: String) {
        tv_report_format.text = reportFormat
    }

    override fun selectedReport(report: String) {
        tv_report_choice.text = report
    }

    override fun showReportSuccessToast() {
        showToast(getString(R.string.report_create))
    }

    override fun requestPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_PERMISSION_REQUEST_CODE
            )
            return false
        } else
            return true
    }

    override fun showToast(text: String) {
        fragmentsListener.showToast(text)
    }

    override fun showProgressDialog(show: Boolean) {
        if (pb_report != null) {
            if (show)
                pb_report.visibility = View.VISIBLE
            else
                pb_report.visibility = View.GONE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            WRITE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast("Разрешение выключено")
                } else {
                    showToast("Разрешение включено")
                    reportPresenter.downloadReport()
                }
            }
        }
    }

    override fun textChange(newText: String) {
        if (reportAdapter != null)
            reportAdapter.let { it?.filter?.filter(newText) }
    }

    override fun closeList() {
    }

    override fun closeSearchView() {
    }

    override fun download(reportId: Int, filename: String) {
        reportPresenter.saveData(reportId, filename)
        if (requestPermissions())
            reportPresenter.downloadReport()
    }

    override fun checkPermission(): Boolean {
        return requestPermissions()
    }
}