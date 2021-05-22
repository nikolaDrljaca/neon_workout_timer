package com.drbrosdev.workouttimer.ui.home

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.drbrosdev.workouttimer.R
import com.drbrosdev.workouttimer.databinding.FragmentHomeBinding
import com.drbrosdev.workouttimer.ui.savedworkouts.SavedWorkoutsViewModel
import com.drbrosdev.workouttimer.util.*
import com.google.android.material.transition.MaterialSharedAxis


class HomeFragment: Fragment(R.layout.fragment_home) {
    private val viewModel: SavedWorkoutsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)

        viewModel.workout.observe(viewLifecycleOwner) { workout ->
            binding.apply {
                etTimerSets.setText("${workout.sets}")
                etTimerWork.setText("${workout.work}")
                etTimerRest.setText("${workout.rest}")
            }
        }

        //setting the progress bar values animated
        binding.apply {
            etTimerWork.addTextChangedListener { text ->
                val progressBarValue = if (text.toString().isNotEmpty()) {
                    (text.toString().toInt() * 100) / WORK_BASE_RATE
                } else 1

                ObjectAnimator.ofInt(progressBarWork, "progress", progressBarValue)
                        .setDuration(300)
                        .start()
            }

            etTimerRest.addTextChangedListener { text ->
                val progressBarValue = if (text.toString().isNotEmpty()) {
                    (text.toString().toInt() * 100) / REST_BASE_RATE
                } else 1

                ObjectAnimator.ofInt(progressBarRest, "progress", progressBarValue)
                        .setDuration(300)
                        .start()
            }

            etTimerSets.addTextChangedListener { text ->
                val progressBarValue = if (text.toString().isNotEmpty()) {
                    (text.toString().toInt() * 100) / SETS_BASE_RATE
                } else 1

                ObjectAnimator.ofInt(progressBarSets, "progress", progressBarValue)
                        .setDuration(300)
                        .start()
            }
        }

        //click listeners for buttons (navigation) and the different transitions
        binding.apply {
            tvSavedTimers.setOnClickListener {
                exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
                val action = HomeFragmentDirections.actionHomeFragmentToSavedWorkoutsFragment()
                findNavController().navigate(action)
            }
            tvSaveWorkout.setOnClickListener {
                val triplet = Triple(
                        first = if(etTimerSets.text.toString().isNotEmpty())
                            etTimerSets.text.toString().toInt() else 5,
                        second = if (etTimerWork.text.toString().isNotEmpty())
                            etTimerWork.text.toString().toInt() else 45,
                        third = if (etTimerRest.text.toString().isNotEmpty())
                            etTimerRest.text.toString().toInt() else 15
                )
                val bundle = bundleOf("triplet" to triplet)
                findNavController().navigate(R.id.action_homeFragment_to_saveNewWorkoutFragment, bundle)
            }
            tvStartTimer.setOnClickListener {
                //define the material transitions here?
                exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
                reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

                val bundle = bundleOf(START_TIMER_BUNDLE_KEY to Triple(
                        first = etTimerSets.text.toString().toInt(),
                        second = etTimerWork.text.toString().toInt(),
                        third = etTimerRest.text.toString().toInt()
                ))
                findNavController().navigate(R.id.action_homeFragment_to_timerFragment, bundle)
            }
            tvAboutScreen.setOnClickListener {
                exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

                findNavController().navigate(R.id.action_homeFragment_to_aboutFragment)
            }
        }
    }
}