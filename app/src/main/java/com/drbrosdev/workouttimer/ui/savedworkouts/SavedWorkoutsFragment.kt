package com.drbrosdev.workouttimer.ui.savedworkouts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.drbrosdev.workouttimer.R
import com.drbrosdev.workouttimer.databinding.FragmentSavedWorkoutsBinding
import com.google.android.material.transition.MaterialSharedAxis


class SavedWorkoutsFragment: Fragment(R.layout.fragment_saved_workouts),
        WorkoutListAdapter.OnClickItemListener {
    private val viewModel: SavedWorkoutsViewModel by activityViewModels()
    private lateinit var adapter: WorkoutListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSavedWorkoutsBinding.bind(view)
        adapter = WorkoutListAdapter(this)

        binding.apply {
            rvSavedWorkouts.adapter = adapter
            rvSavedWorkouts.layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.workoutList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onItemClicked(workoutId: Int) {
        viewModel.onWorkoutItemClicked(workoutId)
        findNavController().navigateUp()
    }

    override fun onDeleteClicked(position: Int) {
        viewModel.onDeleteWorkoutClicked(position)
    }
}