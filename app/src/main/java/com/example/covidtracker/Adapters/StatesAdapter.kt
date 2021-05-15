package com.example.covidtracker.Adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.covidtracker.Fragments.StateFragment
import com.example.covidtracker.Models.India.Statewise
import com.example.covidtracker.Models.India.Tested
import com.example.covidtracker.Models.World.countryDataItem
import com.example.covidtracker.R
import kotlinx.android.synthetic.main.each_state.view.*

class StatesAdapter(val list : ArrayList<Statewise>, val tests : ArrayList<Tested>): RecyclerView.Adapter<StatesAdapter.StatesViewHolder>(), Filterable {

    private val filterList = list
    private var usedList = list

    class StatesViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatesViewHolder {
        return StatesViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.each_state, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StatesViewHolder, position: Int) {
//        if (list[position].state != "Total") {
        Log.d("check list", usedList.toString())
        holder.view.name.text = usedList[position].state + " (" + usedList[position].statecode + ")"
            holder.view.cases.text = usedList[position].confirmed
            val unit1 = usedList[position]
            val unit2 = tests[position]

            holder.view.open_info.setOnClickListener {
                val activity = it.context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_up_anim, R.anim.fade_out, R.anim.fade_out, R.anim.slide_down_anim)
                        .replace(R.id.fragment_container, StateFragment(unit1, unit2))
                        .addToBackStack("state")
                        .commit()
            }
//        }
    }

    override fun getItemCount() = usedList.size


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

                    val filteredList = ArrayList<Statewise>()

                    for (i in filterList){
                        if(i.state.lowercase().contains(searchChar) || i.statecode.lowercase().contains(searchChar)){
                            filteredList.add(i)
                        }
                    }

                    filterResult.count = filteredList.size
                    filterResult.values = filteredList
                }
                return  filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                Log.d("state adapter", results.toString())
                usedList = results?.values as ArrayList<Statewise>
                notifyDataSetChanged()
            }

        }
    }

}