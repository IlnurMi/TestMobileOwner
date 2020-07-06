package com.ilnur.mobileowner.presentation.ui.report

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ilnur.mobileowner.R
import kotlinx.android.synthetic.main.report_pager_frgament.*
import com.ilnur.mobileowner.interfaces.DrawerFragmentInterface
import com.ilnur.mobileowner.interfaces.views.report.ReportPagerView

class ReportPagerFragment(): Fragment(), ReportPagerView, DrawerFragmentInterface {

    private var reportPagerAdapter: ReportPagerAdapter? = null

    var firstFragment: ReportFragment = ReportFragment()
    var secondFragment: ReportAddFragment = ReportAddFragment()

    companion object {
        var INSTANCE: ReportPagerFragment? = null

        fun getInstance(): ReportPagerFragment {
            if (INSTANCE == null)
                INSTANCE = ReportPagerFragment()
            return INSTANCE!!
        }

        fun newInstance(): ReportPagerFragment {
            INSTANCE = ReportPagerFragment()
            return INSTANCE!!
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.report_pager_frgament, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVars()
    }


    override fun initVars(){
        reportPagerAdapter = ReportPagerAdapter(childFragmentManager)
        reportPagerAdapter!!.addFragment(firstFragment, getString(R.string.reports))
        reportPagerAdapter!!.addFragment(secondFragment, getString(R.string.reports_order))
        vp_reports.adapter = reportPagerAdapter
        tl_reports.setupWithViewPager(vp_reports)
    }
}