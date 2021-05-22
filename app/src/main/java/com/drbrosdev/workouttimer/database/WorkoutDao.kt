package com.drbrosdev.workouttimer.database

import androidx.room.*
import com.drbrosdev.workouttimer.models.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Query("select * from workout_table")
    fun getWorkoutList(): Flow<List<Workout>>

    @Query("select * from workout_table where id=:id")
    suspend fun getWorkout(id: Int): Workout

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout)
}