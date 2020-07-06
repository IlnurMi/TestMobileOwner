package com.ilnur.mobileowner.presentation.ui.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.google.android.gms.maps.model.LatLng
import com.ilnur.mobileowner.R
import com.ilnur.mobileowner.domain.models.response.DefaultRequest
import com.ilnur.mobileowner.domain.models.response.route.CarOnMap
import com.ilnur.mobileowner.interfaces.views.map.MapFragmentListeners

class MyMapFragmentAdapter(
    var cars: MutableList<CarOnMap>,
    private var ctx: Context,
    var listeners: MapFragmentListeners
) : RecyclerView.Adapter<MyMapFragmentAdapter.CarsViewHolder>(), Filterable {
    var tempList: MutableList<CarOnMap>? = null
    var filterList: MutableList<DefaultRequest>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarsViewHolder {
        var view = LayoutInflater.from(ctx).inflate(R.layout.car_on_maps_item, parent, false)
        return CarsViewHolder(view)
    }

    fun addItems(list: List<CarOnMap>) {
        cars.clear()
        cars.addAll(list)
        tempList = cars
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if (charString == null) {
                    cars = tempList!!
                } else {
                    filterList = ArrayList()
                    for (item in tempList!!) {
                        if (item.name?.toLowerCase()!!.contains(charString))
                            (filterList as ArrayList<CarOnMap>).add(item)
                    }
                }
                var filterResults: FilterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results?.values != null) {
                    cars = (results?.values as? MutableList<CarOnMap>)!!
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: CarsViewHolder, position: Int) {
        val item = cars[position]
        holder.tvCarsName.text = item.name
        holder.bind(item)
    }

    inner class CarsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCarsName = itemView.findViewById<TextView>(R.id.tv_carName)!!
        private var rlItemsCar = itemView.findViewById<ConstraintLayout>(R.id.rl_items_car)

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(data: CarOnMap) {

            rlItemsCar.setOnClickListener {
                var latLng = LatLng(data.lat!!.toDouble(), data.lon!!.toDouble())
                listeners.moveCamera(latLng)
            }
        }

    }
}