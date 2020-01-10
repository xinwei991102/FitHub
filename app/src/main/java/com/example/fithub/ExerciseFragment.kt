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
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.bounce_anim_quick))
            var intent = Intent(requireContext(), ExerciseActivity::class.java)
            intent.putExtra("exercise_name",getString(R.string.aerobic_exercise))
            intent.putExtra("met_score", 5.5)
            requireContext().startActivity(intent)
        }

        buttonCore.setOnClickListener{
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.bounce_anim_quick))
            var intent = Intent(requireContext(), ExerciseActivity::class.java)
            intent.putExtra("exercise_name",getString(R.string.core_exercise))
            intent.putExtra("met_score", 4.0)
            requireContext().startActivity(intent)
        }

        buttonArm.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.bounce_anim_quick))
            var intent = Intent(requireContext(), ExerciseActivity::class.java)
            intent.putExtra("exercise_name",getString(R.string.arm_exercise))
            intent.putExtra("met_score", 3.5)
            requireContext().startActivity(intent)
        }

        buttonLeg.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.bounce_anim_quick))
            var intent = Intent(requireContext(), ExerciseActivity::class.java)
            intent.putExtra("exercise_name",getString(R.string.leg_exercise))
            intent.putExtra("met_score", 4.0)
            requireContext().startActivity(intent)
        }

        buttonTest.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.bounce_anim_quick))
            var intent = Intent(requireContext(), ExerciseActivity::class.java)
            intent.putExtra("exercise_name",getString(R.string.testing_exercise))
            intent.putExtra("met_score", 100.0)
            requireContext().startActivity(intent)
        }
    }
}