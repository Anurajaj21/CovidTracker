package com.example.covidtracker.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.covidtracker.Fragments.CountryFragment
import com.example.covidtracker.Models.countryDataItem
import com.example.covidtracker.R
import kotlinx.android.synthetic.main.each_country.view.*

class CountriesAdapter(val list: ArrayList<countryDataItem>): RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>() {

    class CountriesViewHolder(val view: View) : RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        return CountriesViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.each_country, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.view.name.text = list[position].country
        holder.view.cases.text = list[position].cases.toString()
        val url = list[position].flag

        Glide.with(holder.view.flag)
                .load(url)
                .placeholder(R.color.dark_gray)
                .into(holder.view.flag)
        holder.view.open_info.setOnClickListener {
            val activity = it.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_up_anim, R.anim.fade_out, R.anim.fade_out, R.anim.slide_down_anim)
                .replace(R.id.fragment_container, CountryFragment())
                .addToBackStack("country")
                .commit()
        }
    }

    override fun getItemCount() = list.size

}