package com.example.fithub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_exercise.*


class ExerciseFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_exercise, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        buttonAerobic.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.bounce_anim))
            var intent = Intent(requireContext(), ExerciseActivity::class.java)
            requireContext().startActivity(intent)
        }
    }
}