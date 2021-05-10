package com.example.covidtracker.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.covidtracker.Fragments.HomeFragment
import com.example.covidtracker.Fragments.IndiaFragment
import com.example.covidtracker.Fragments.WorldFragment
import com.example.covidtracker.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.each_state.view.*

class MainActivity : AppCompatActivity() {
    val fragmentManager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
        bottom_nav.setOnNavigationItemSelectedListener { item->
            when(item.itemId){
                R.id.home -> {
                    changeFragment(HomeFragment())
                    true
                }
                R.id.india -> {
                    changeFragment(IndiaFragment())
                    true
                }
                R.id.world -> {
                    changeFragment(WorldFragment())
                    true
                }
                else -> false
            }
        }
    }


    private fun changeFragment(dest : Fragment) {
        val fragmentTransition = fragmentManager.beginTransaction()
        fragmentTransition.setCustomAnimations(R.anim.slide_left_anim, R.anim.fade_out)
        fragmentTransition.replace(R.id.fragment_container, dest)
        fragmentTransition.setReorderingAllowed(true)
        fragmentManager.popBackStack()
        fragmentTransition.commit()

    }
}