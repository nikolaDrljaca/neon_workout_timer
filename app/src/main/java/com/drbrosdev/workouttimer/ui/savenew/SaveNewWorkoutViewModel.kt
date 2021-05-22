package com.drbrosdev.workouttimer.ui.savenew

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.drbrosdev.workouttimer.database.WorkoutDatabase
import com.drbrosdev.workouttimer.models.Workout
import kotlinx.coroutines.launch

class SaveNewWorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val workoutDao = WorkoutDatabase.getDatabase(application).workoutDao()

    fun insert(
        sets: Int,
        rest: Int,
        work: Int,
    ) = viewModelScope.launch {
        workoutDao.insertWorkout(Workout(
            sets = sets,
            rest = rest,
            work = work
        ))
    }

    fun insert(
        sets: Int,
        rest: Int,
        work: Int,
        name: String
    ) = viewModelScope.launch {
        workoutDao.insertWorkout(Workout(
            sets = sets,
            rest = rest,
            work = work,
            name = name
        ))
    }
}