package com.thazin.citiesapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.thazin.citiesapp.R
import com.thazin.citiesapp.common.AppConstants
import com.thazin.citiesapp.data.City
import com.thazin.citiesapp.ui.activity.MapsActivity
import kotlinx.android.synthetic.main.item_city.view.*
import java.util.*

class CityAdapter(private var cityList: List<City>, private val context: Context) : RecyclerView.Adapter<CityAdapter.CityViewHolder>(), Filterable {
    private var filterList: List<City>
    private var startTime = 0L
    private var searchCount = 0
    private var stackHMCityList : Stack<HashMap<String, List<City>>> = Stack()

    init {
        filterList = cityList
    }

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindItems(city: City, context: Context){
            itemView.tv_title!!.text = "${city.name}, ${city.country}"
            "lon ${city.coord.lon}, lat ${city.coord.lat}".also { itemView.tv_sub_title!!.text = it }
            itemView.setOnClickListener {
                val mapIntent = Intent(context, MapsActivity::class.java)
                mapIntent.putExtra(AppConstants.LONGITUDE, city.coord.lon)
                mapIntent.putExtra(AppConstants.LATITUDE, city.coord.lat)
                mapIntent.putExtra(AppConstants.CITY, city.name)
                context.startActivity(mapIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bindItems(filterList[position], context)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    /** Linear Search */
    /*override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSearch: CharSequence?): FilterResults {
                startTime = System.currentTimeMillis()
                filterList = if (charSearch.toString().isEmpty()) {
                    cityList
                } else {
                    val result = ArrayList<City>()
                    for (city in cityList) {
                        if (city.name.lowercase().contains(charSearch.toString().lowercase())) {
                            result.add(city)
                        }
                    }
                    result
                }
                val filterResult = FilterResults()
                filterResult.values = filterList
                return filterResult
            }

            override fun publishResults(p0: CharSequence?, filterResult: FilterResults?) {
                filterList = filterResult?.values as List<City>
                Log.d(CityAdapter::class.simpleName, "Execution time "+(System.currentTimeMillis() - startTime))
                notifyDataSetChanged()
            }
        }
    }*/

    /**
     * Faster than linear Search
     */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSearch: CharSequence?): FilterResults {
                startTime = System.currentTimeMillis()
                if (searchCount > charSearch!!.length && stackHMCityList.isNotEmpty()) {
                    stackHMCityList.pop()
                }

                filterList = if (charSearch.toString().isEmpty()) {
                    cityList
                } else {
                    val result = if (filterList.isNotEmpty() && searchCount < charSearch!!.length) {
                        search(charSearch, filterList)
                    } else { //case for clear search char
                        val hashMap = stackHMCityList.pop()
                        if (hashMap.containsKey(charSearch)) {
                            hashMap.getValue(charSearch.toString())
                        } else {
                            search(charSearch, cityList)
                        }

                    }
                    result
                }
                val filterResult = FilterResults()
                filterResult.values = filterList
                searchCount = charSearch!!.length
                return filterResult
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSearch: CharSequence?, filterResult: FilterResults?) {
                filterList = filterResult?.values as List<City>
                Log.d(CityAdapter::class.simpleName, "Execution time "+(System.currentTimeMillis() - startTime))
                val hashMap:HashMap<String,List<City>> = HashMap()
                hashMap[charSearch.toString()] = filterList
                stackHMCityList.add(hashMap)
                notifyDataSetChanged()
            }
        }
    }

    private fun search(charSearch: CharSequence?, cities: List<City>): ArrayList<City> {
        val resultList = ArrayList<City>()
        if (cities.isNotEmpty()) {
            for (city in cities) {
                if (city.name.lowercase().contains(charSearch.toString().lowercase())) {
                    resultList.add(city)
                }
            }
        }
        return resultList
    }
}


