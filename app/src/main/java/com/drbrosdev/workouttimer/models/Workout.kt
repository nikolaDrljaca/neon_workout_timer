package com.drbrosdev.workouttimer.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_table")
data class Workout(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        val name: String = "Workout",
        val sets: Int,
        val work: Int,
        val rest: Int
) {
    fun getStats() = "$sets | $work | $rest"
}