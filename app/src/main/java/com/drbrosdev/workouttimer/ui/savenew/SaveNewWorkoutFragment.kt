package com.drbrosdev.workouttimer.ui.savenew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.drbrosdev.workouttimer.R
import com.drbrosdev.workouttimer.databinding.FragmentSaveNewWorkoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SaveNewWorkoutFragment : BottomSheetDialogFragment() {
    private lateinit var triple: Triple<Int, Int, Int>
    private val viewModel: SaveNewWorkoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            triple = bundle.get("triplet") as Triple<Int, Int, Int>
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_save_new_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSaveNewWorkoutBinding.bind(view)

        binding.apply {
            etWorkoutSets.editText?.setText("${triple.first}")
            etWorkoutWork.editText?.setText("${triple.second}")
            etWorkoutRest.editText?.setText("${triple.third}")

            btnSave.setOnClickListener {
                etWorkoutName.editText?.let {
                    if (it.text.isEmpty()) viewModel.insert(triple.first, triple.second, triple.third)
                    else viewModel.insert(
                            sets = triple.first,
                            work = triple.second,
                            rest = triple.third,
                            name = it.text.toString()
                    )
                    //close the bottom sheet fragment and display toast message
                    Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }
}