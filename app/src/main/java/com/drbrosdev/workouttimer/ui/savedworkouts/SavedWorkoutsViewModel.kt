package com.drbrosdev.workouttimer.ui.savedworkouts

import android.app.Application
import androidx.lifecycle.*
import com.drbrosdev.workouttimer.database.WorkoutDatabase
import com.drbrosdev.workouttimer.models.Workout
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SavedWorkoutsViewModel(application: Application): AndroidViewModel(application) {
    private val workoutDao = WorkoutDatabase.getDatabase(application).workoutDao()

    val workoutList = workoutDao.getWorkoutList().asLiveData()

    private val _workout = MutableLiveData<Workout>()
    val workout: LiveData<Workout> get() = _workout

    fun onDeleteWorkoutClicked(position: Int) = viewModelScope.launch {
        workoutList.value?.let { list ->
            workoutDao.deleteWorkout(list[position])
        }
    }

    fun onWorkoutItemClicked(workoutId: Int) = viewModelScope.launch {
        _workout.value = workoutDao.getWorkout(workoutId)
    }
}