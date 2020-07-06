package com.ilnur.mobileowner.presentation.ui.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ilnur.mobileowner.R
import com.ilnur.mobileowner.domain.models.request.ReportCarsList
import com.ilnur.mobileowner.interfaces.views.report.ReportCarAdapterListener

class ReportCarsAdapter(
    var cars: MutableList<ReportCarsList>,
    reportCarAdapterListener: ReportCarAdapterListener
) : RecyclerView.Adapter<ReportCarsAdapter.CarsViewHolder>(), Filterable {
    var tempList = cars
    var filterList: MutableList<ReportCarsList>? = null
    var listener: ReportCarAdapterListener = reportCarAdapterListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsViewHolder {
        return CarsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.report_cars_dialog_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return cars!!.size
    }

    override fun onBindViewHolder(holder: CarsViewHolder, position: Int) {
        cars?.get(position)?.let { holder.bind(it) }
    }

    inner class CarsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvReportCarsName = itemView.findViewById<TextView>(R.id.tv_report_cars_name)
        private var rlItemsCars = itemView.findViewById<ConstraintLayout>(R.id.rl_items_cars)
        private var ivStatusCars = itemView.findViewById<ImageView>(R.id.iv_status_cars)

        fun bind(data: ReportCarsList) {
            tvReportCarsName.text = data.name
            rlItemsCars.setOnClickListener {
                ivStatusCars.setImageResource(R.drawable.ic_correct)
                listener.selectCar(data)
                listener.dialogDismiss()
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                if (charSequence == null) {
                    cars = tempList!!
                } else {
                    filterList = ArrayList()
                    for (item in tempList!!) {
                        if (item.name.toLowerCase()!!.contains(charSequence))
                            (filterList as ArrayList<ReportCarsList>).add(item)
                    }
                }
                var filterResults: FilterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                if (results?.values != null) {
                    cars = (results?.values as MutableList<ReportCarsList>)!!
                }
                notifyDataSetChanged()
            }
        }
    }
}