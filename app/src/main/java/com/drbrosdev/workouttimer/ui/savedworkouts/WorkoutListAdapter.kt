package com.drbrosdev.workouttimer.ui.savedworkouts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drbrosdev.workouttimer.databinding.ItemWorkoutTimerBinding
import com.drbrosdev.workouttimer.models.Workout

class WorkoutListAdapter(private val listener: OnClickItemListener) :
        ListAdapter<Workout, WorkoutListAdapter.ViewHolder>(DiffCallback()) {

    interface OnClickItemListener {
        fun onItemClicked(workoutId: Int)
        fun onDeleteClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWorkoutTimerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemWorkoutTimerBinding) :
            RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                tvDeleteTimer.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onDeleteClicked(adapterPosition)
                    }
                }
                card.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onItemClicked(getItem(adapterPosition).id)
                    }
                }
            }
        }

        fun bind(workout: Workout) {
            binding.apply {
                tvWorkoutName.text = workout.name
                tvTimeWorkout.text = workout.getStats()
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout) =
                (oldItem.id == newItem.id)

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout) =
                oldItem == newItem

    }
}