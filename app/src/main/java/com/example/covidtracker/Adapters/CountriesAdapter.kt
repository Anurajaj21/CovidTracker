   package com.example.covidtracker.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.covidtracker.Fragments.CountryFragment
import com.example.covidtracker.Models.World.countryDataItem
import com.example.covidtracker.R
import kotlinx.android.synthetic.main.each_country.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

   @Suppress("UNCHECKED_CAST")
class CountriesAdapter(var list: ArrayList<countryDataItem>): RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>(), Filterable {

    private val filterList  = list
    private var usedList : ArrayList<countryDataItem> = list
    class CountriesViewHolder(val view: View) : RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        return CountriesViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.each_country, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.view.name.text = usedList[position].country
        holder.view.cases.text = usedList[position].cases.toString()
        val url = usedList[position].countryInfo.flag
        val unit = usedList[position]
        GlobalScope.launch(Dispatchers.Main) {
            try {
                Glide.with(holder.view.flag)
                        .load(url)
                        .placeholder(R.color.dark_gray)
                        .into(holder.view.flag)

                holder.view.open_info.setOnClickListener {
                    val activity = it.context as AppCompatActivity
                    activity.supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_up_anim, R.anim.fade_out, R.anim.fade_out, R.anim.slide_down_anim)
                            .replace(R.id.fragment_container, CountryFragment(unit))
                            .addToBackStack("country")
                            .commit()
                }
            }catch (e: Exception){
                Log.d("flag", e.message!!)
            }
        }
    }

    override fun getItemCount() = usedList.size

    @ExperimentalStdlibApi
    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val filterResult = FilterResults()
                if(charSequence == null || charSequence.length < 0){
                    Log.d("filterlist", filterResult.toString())
                    filterResult.count = filterList.size
                    filterResult.values = filterList
                }else{
                    val searchChar = charSequence.toString().lowercase()

                    val filteredList = ArrayList<countryDataItem>()

                    for (i in filterList){
                        if(i.country.lowercase().contains(searchChar)){
                            filteredList.add(i)
                        }
                    }

                    filterResult.count = filteredList.size
                    filterResult.values = filteredList
                }
                return  filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                Log.d("searchView", results?.values.toString())
                if(results?.values != null) {
                    usedList = (results.values as? ArrayList<countryDataItem>)!!
                    Log.d("searched", usedList.toString())
                }
                notifyDataSetChanged()
            }
        }
    }


}