package com.example.covidtracker.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.covidtracker.Fragments.DistrictFragment
import com.example.covidtracker.Fragments.StateFragment
import com.example.covidtracker.Models.DistrictData
import com.example.covidtracker.Models.Statewise
import com.example.covidtracker.R
import kotlinx.android.synthetic.main.each_state.view.*

class DistrictAdapter(val list : ArrayList<DistrictData>): RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder>() {

    class DistrictViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictViewHolder {
        return DistrictViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.each_state, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DistrictViewHolder, position: Int) {
        holder.view.name.text = list[position].district
        holder.view.cases.text = list[position].active.toString()

        holder.view.open_info.setOnClickListener {
            val activity = it.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_up_anim, R.anim.fade_out, R.anim.fade_out, R.anim.slide_down_anim)
                .replace(R.id.fragment_container, DistrictFragment())
                .addToBackStack("state")
                .commit()
        }
    }

    override fun getItemCount() = list.size

}