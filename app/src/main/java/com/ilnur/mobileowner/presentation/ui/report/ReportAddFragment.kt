package com.ilnur.mobileowner.presentation.ui.report

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilnur.mobileowner.R
import kotlinx.android.synthetic.main.report_add_fragment.*
import kotlinx.android.synthetic.main.report_add_fragment.btn_report_order
import kotlinx.android.synthetic.main.report_car_dialog_fragment.view.*
import com.ilnur.mobileowner.data.network.ApiClient
import com.ilnur.mobileowner.data.repositories.report.ReportAddRepository
import com.ilnur.mobileowner.domain.interactors.report.ReportAddInteractor
import com.ilnur.mobileowner.domain.models.request.ReportCarsList
import com.ilnur.mobileowner.domain.models.response.DefaultRequest
import com.ilnur.mobileowner.interfaces.BackPressedInterface
import com.ilnur.mobileowner.interfaces.FragmentsListener
import com.ilnur.mobileowner.interfaces.views.ReplaceFragmentInterface
import com.ilnur.mobileowner.interfaces.views.report.ReportAddFragmentView
import com.ilnur.mobileowner.interfaces.views.report.ReportCarAdapterListener
import com.ilnur.mobileowner.interfaces.views.report.ReportDateTimeListener
import com.ilnur.mobileowner.presentation.presenters.report.ReportAddPresenter
import com.ilnur.mobileowner.presentation.ui.DatePickerFragment
import com.ilnur.mobileowner.presentation.ui.TimePickerFragment
import com.ilnur.mobileowner.presentation.ui.extensions.changeDateTime
import com.ilnur.mobileowner.data.utils.ConstantUtils
import java.lang.ClassCastException
import java.util.*


class ReportAddFragment : Fragment(), ReplaceFragmentInterface, BackPressedInterface,
    ReportAddFragmentView, ReportDateTimeListener, ReportCarAdapterListener {
    private lateinit var fragmentsListener: FragmentsListener
    private lateinit var reportAddPresenter: ReportAddPresenter
    private var reportCarsAdapter: ArrayAdapter<ReportCarsList>? = null
    private var reportTypeAdapter: ArrayAdapter<DefaultRequest>? = null
    private var reportCarList: List<ReportCarsList>? = null
    private lateinit var dialog: AlertDialog

    companion object {
        var INSTANCE: ReportAddFragment? = null

        fun geInstance(): ReportAddFragment {
            if (INSTANCE == null)
                INSTANCE = ReportAddFragment()
            return INSTANCE!!
        }

        fun newInstance(): ReportAddFragment {
            INSTANCE = ReportAddFragment()
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
        return inflater.inflate(R.layout.report_add_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVars()
        clickListener()
    }

    override fun onBackPressed() {
        fragmentsListener.replaceBaseFragment(ReportFragment.getInstance())
    }

    override fun initVars() {
        fragmentsListener.showSearchView(false)
        reportAddPresenter = ReportAddPresenter(
            ReportAddInteractor(
                ReportAddRepository(
                    ApiClient().getTestRestClient(requireContext()),
                    ApiClient().getTestDownloadRestClient(requireContext())
                )
            )
        )
        reportAddPresenter.setView(this)
        reportCarList = ArrayList()
        reportAddPresenter.getReportType()
        reportAddPresenter.getCarList()
    }

    override fun clickListener() {

        cl_report_type.setOnClickListener {
            showReportTypeAlertDialog()
        }

        cl_cars.setOnClickListener {
            if (reportCarList!!.isEmpty())
                showToast(getString(R.string.list_empty))
            else
                showAlertDialog()
        }

        cl_date_start.setOnClickListener {
            openDataPickerFragment(tv_date_start_new)
        }

        cl_date_end.setOnClickListener {
            openDataPickerFragment(tv_date_end_new)
        }

        btn_report_order.setOnClickListener {

            val rgx = Regex("""\d{2}.\d{2}.\d{4} \d{2}:\d{2}""")
            val textStart = tv_date_start_new.text.toString()
            val textEnd = tv_date_end_new.text.toString()

            if (tv_report_type_new.text.isEmpty() || tv_cars_type_new.text.isEmpty() ||
                tv_date_start_new.text.isEmpty() || tv_date_end_new.text.isEmpty()) {
                showToast(getString(R.string.toast_empty_fields))
            } else if (!textStart.matches(rgx) || !textEnd.matches(rgx)) {
                showToast("Необходимо указать время.")
            } else {
                val start = tv_date_start_new.text.toString().changeDateTime(ConstantUtils.DATE_BEGIN)
                val end = tv_date_end_new.text.toString().changeDateTime(ConstantUtils.DATE_END)
                if (reportAddPresenter.comparisonDate(start, end)) {
                    reportAddPresenter.postReport(start, end)
                } else {
                    showToast(getString(R.string.toast_incorrect_date_period))
                }
            }
        }
    }

    //New method create Alert Dialog
    private fun showAlertDialog() {
        dialog = AlertDialog.Builder(requireContext()).create()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        val view: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.report_car_dialog_fragment, null)

        view.apply {
            val reportCarsAdapter = ReportCarsAdapter(
                reportCarList as MutableList<ReportCarsList>,
                this@ReportAddFragment
            )
            rv_reports_car.layoutManager = LinearLayoutManager(requireContext())
            rv_reports_car.adapter = reportCarsAdapter as ReportCarsAdapter

            search_view_report.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    reportCarsAdapter.let { it.filter.filter(p0) }
                    return true
                }
            })

            search_view_report.setOnClickListener {
            }
        }

        dialog.apply {
            setView(view)
            show()
        }
    }

    override fun showToast(text: String) {
        fragmentsListener.showToast(text)
    }

    override fun showReportTypeAlertDialog() {
        if (reportTypeAdapter != null) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.select_report))
            builder.setSingleChoiceItems(reportTypeAdapter, -1) { dialog, which ->
                val listView = (dialog as AlertDialog).listView
                val reportType: DefaultRequest = listView.getItemAtPosition(which) as DefaultRequest
                reportAddPresenter.reportTypeAlertClick(reportType)
                dialog.dismiss()
            }.create().show()
        } else {
            showToast(getString(R.string.list_empty))
        }
    }

    override fun showReportCarsAlertDialog() {
        if (reportCarsAdapter != null) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.select_car))
            builder.setSingleChoiceItems(reportCarsAdapter, -1) { dialog, which ->
                val listView = (dialog as AlertDialog).listView
                val reportCars: ReportCarsList = listView.getItemAtPosition(which) as ReportCarsList
                reportAddPresenter.reportCarsAlertClick(reportCars)
                dialog.dismiss()
            }.create().show()
        } else {
            showToast(getString(R.string.list_empty))
        }
    }

    override fun openDataPickerFragment(textView: TextView) {
        var datePickerFragment = DatePickerFragment(textView, this)
        datePickerFragment.show(requireFragmentManager(), "date")
    }

    override fun openTimePickerFragment(textView: TextView) {
        var timePickerFragment = TimePickerFragment(textView)
        fragmentManager?.let { timePickerFragment.show(it, "time") }
    }

    override fun populateReportTypeAdapter(typeList: List<DefaultRequest>) {
        reportTypeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.select_dialog_singlechoice, typeList)
    }

    override fun populateReportCarsAdapter(carsList: List<ReportCarsList>) {
        reportCarsAdapter =
            ArrayAdapter(requireContext(), android.R.layout.select_dialog_singlechoice, carsList)
        reportCarList = carsList!!
    }

    override fun selectedReportType(reportType: String) {
        tv_report_type_new.text = reportType
    }

    override fun selectedReportCars(reportCars: String) {
        tv_cars_type_new.text = reportCars
    }

    override fun showReportSuccessToast() {
        showToast(getString(R.string.report_order_message))
        clearTextView()
    }

    override fun clearTextView() {
        tv_report_type_new.text = null
        tv_cars_type_new.text = null
        tv_date_start_new.text = null
        tv_date_end_new.text = null
    }

    override fun startTimePicker(textView: TextView) {
        openTimePickerFragment(textView)
    }

    override fun startTimePicker(textView: TextView, date: String) {
        //TODO("Not yet implemented")
    }

    override fun selectCar(car: ReportCarsList) {
        reportAddPresenter.reportCarsAlertClick(car)
    }

    override fun dialogDismiss() {
        dialog.dismiss()
    }
}