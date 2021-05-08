package com.example.covidtracker.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.RenderMode
import com.example.covidtracker.R


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val animationView = view.findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        animationView.setRenderMode(RenderMode.SOFTWARE)
        return view
    }

}