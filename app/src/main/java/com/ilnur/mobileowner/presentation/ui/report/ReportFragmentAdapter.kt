package com.ilnur.mobileowner.presentation.ui.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ilnur.mobileowner.R
import kotlinx.android.synthetic.main.new_report_item.view.*
import com.ilnur.mobileowner.domain.models.request.ReportRequest
import com.ilnur.mobileowner.interfaces.views.report.ReportAdapterListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReportFragmentAdapter(var reports: MutableList<ReportRequest>,
                            private var reportAdapterListener: ReportAdapterListener
): RecyclerView.Adapter<ReportFragmentAdapter.ReportViewHolder>(), Filterable {
    private var tempList: MutableList<ReportRequest>? = null
    private var filterList: MutableList<ReportRequest>? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReportViewHolder {
        return ReportViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.new_report_item, parent, false))
    }

    override fun getItemCount(): Int {
        return reports.size
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val item = reports[position]
        holder.bind(item)
    }

    fun addItems(list: List<ReportRequest>){
        reports.clear()
        reports.addAll(list);
        tempList = reports
        notifyDataSetChanged()
    }

    inner class ReportViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var tvReportName = itemView.findViewById<TextView>(R.id.tv_report_name)
        private var reportDownload = itemView.findViewById<ImageView>(R.id.report_download)
        private var reportStatus = itemView.findViewById<ImageView>(R.id.iv_status_report)

        init{

        }

        fun bind(data: ReportRequest){
            tvReportName.text = changeReportTitle(data.report.toString())

            itemView.apply {
                val period = data.report?.substringAfter("даты ") ?: ""
                tv_report_period.text = "Период: $period"
                //дата создания отчёта
                val dateCreate = formattedDateCreate(data.date.toString())
                tv_report_date_create.text = "Создано: $dateCreate"
            }

            if (data.readiness!!){
                reportStatus.setImageResource(R.drawable.ic_circulars)
                reportDownload.setImageResource(R.drawable.ic_down)
            }else{
                reportStatus.setImageResource(R.drawable.ic_circulars_red)
                reportDownload.setImageResource(R.drawable.ic_downgrey)
            }

            reportDownload.setOnClickListener {
                if (data.readiness!!)
                    reportAdapterListener.download(data.reportId!!.toInt(), data.report.toString())
            }
        }

    }

    fun changeReportTitle(text: String): String{
        return text.substringAfter("'").substringBefore(" даты").dropLastWhile { it == ',' }
    }

    fun showDefaultList(): Boolean{
        reports = tempList!!
        notifyDataSetChanged()
        return true
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence
                if (charString == null){
                    reports = tempList!!
                }else{
                    filterList = ArrayList()
                    for (item in tempList!!){
                        if (item.report?.toLowerCase()!!.contains(charString))
                            (filterList as ArrayList<ReportRequest>).add(item)
                    }
                }
                var filterResults: FilterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                if (results?.values != null){
                    reports = (results?.values as MutableList<ReportRequest>)!!
                }
                notifyDataSetChanged()

            }

        }
    }

    //изменяем формат даты создания
    private fun formattedDateCreate(date: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val targetFormat = SimpleDateFormat("HH:mm dd-MM-yyyy")
        val originalDate: Date = originalFormat.parse(date)
        return targetFormat.format(originalDate).toString()
    }
}