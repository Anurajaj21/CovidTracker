package com.example.covidtracker.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.covidtracker.Fragments.DistrictFragment
import com.example.covidtracker.Models.District.DistrictData
import com.example.covidtracker.Models.World.countryDataItem
import com.example.covidtracker.R
import kotlinx.android.synthetic.main.each_state.view.*

class DistrictAdapter(var list : ArrayList<DistrictData>): RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder>(), Filterable {

    private val filterList = list
//    private var usedList = list

    class DistrictViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictViewHolder {
        return DistrictViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.each_state, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DistrictViewHolder, position: Int) {
        holder.view.name.text = list[position].district
        holder.view.cases.text = list[position].confirmed.toString()
        val unit = list[position]

        holder.view.open_info.setOnClickListener {
            val activity = it.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_up_anim, R.anim.fade_out, R.anim.fade_out, R.anim.slide_down_anim)
                .replace(R.id.fragment_container, DistrictFragment(unit))
                .addToBackStack("state")
                .commit()
        }
    }

    override fun getItemCount() = list.size
    @ExperimentalStdlibApi
    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val filterResult = FilterResults()
                if(charSequence == null || charSequence.length < 0){
                    filterResult.count = filterList.size
                    filterResult.values = filterList
                }else{
                    val searchChar = charSequence.toString().lowercase()

                    val filteredList = ArrayList<DistrictData>()

                    for (i in filterList){
                        if(i.district.lowercase().contains(searchChar)){
                            filteredList.add(i)
                        }
                    }

                    filterResult.count = filteredList.size
                    filterResult.values = filteredList
                }
                return  filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                list = results?.values as ArrayList<DistrictData>
                notifyDataSetChanged()
            }

        }
    }

}