package com.example.covidtracker.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.covidtracker.Fragments.StateFragment
import com.example.covidtracker.Models.Statewise
import com.example.covidtracker.Models.Tested
import com.example.covidtracker.R
import kotlinx.android.synthetic.main.each_state.view.*

class StatesAdapter(val list : ArrayList<Statewise>, val tests : ArrayList<Tested>): RecyclerView.Adapter<StatesAdapter.StatesViewHolder>() {

    class StatesViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatesViewHolder {
        return StatesViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.each_state, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StatesViewHolder, position: Int) {
//        if (list[position].state != "Total") {
            holder.view.name.text = list[position].state + " (" + list[position].statecode + ")"
            holder.view.cases.text = list[position].confirmed
            val unit1 = list[position]
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

    override fun getItemCount() = list.size

}