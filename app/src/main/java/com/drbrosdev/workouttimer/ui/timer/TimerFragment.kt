package com.drbrosdev.workouttimer.ui.timer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.drbrosdev.workouttimer.R
import com.drbrosdev.workouttimer.databinding.FragmentTimerBinding
import com.drbrosdev.workouttimer.util.*
import com.google.android.material.transition.MaterialSharedAxis
import kotlin.system.measureNanoTime

class TimerFragment: Fragment(R.layout.fragment_timer) {
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private lateinit var triple: Triple<Int, Int, Int>
    private var workTime: Long = 0
    private var workTimeLeft: Long = 0
    private var restTime: Long = 0
    private var sets = 0

    private lateinit var workTimer: CountDownTimer
    private lateinit var getReadyTimer: CountDownTimer
    private lateinit var restTimer: CountDownTimer

    private var isTimerRunning = true

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

        //get the arguments
        arguments?.let { bundle ->
            triple = bundle.get(START_TIMER_BUNDLE_KEY) as Triple<Int, Int, Int>
            sets = triple.first
            workTime = (triple.second * 1000).toLong()
            restTime = (triple.third * 1000).toLong()
            workTimeLeft = workTime
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTimerBinding.bind(view)
        _binding = FragmentTimerBinding.bind(view)

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.alert_sound)

        //initial text setup
        binding.apply {
            tvStatus.text = "Get Ready!"
            tvTimerSets.text = "Sets: $sets"
            fabStartStop.setImageResource(R.drawable.ic_round_pause_24)
            fabStartStop.hide()
        }

        defineGetReadyTimer()
        defineStartTimer(workTime)
        defineRestTimer()

        getReadyTimer.start()

        //define the timer objects
        binding.fabStartStop.setOnClickListener {
            if (isTimerRunning) {
                //pause the timer
                workTimer.cancel()
                defineStartTimer(workTimeLeft)
                isTimerRunning = false
                binding.fabStartStop.setImageResource(R.drawable.ic_round_play_arrow_24)
            } else {
                workTimer.start()
                isTimerRunning = true
                binding.fabStartStop.setImageResource(R.drawable.ic_round_pause_24)
            }
        }
    }

    // to stop and save the timer if the app went into the background
    // doesn't start the timer automatically, the user has to start it
    // after returning
    override fun onPause() {
        super.onPause()
        workTimer.cancel()
        defineStartTimer(workTimeLeft)
        isTimerRunning = false
        binding.fabStartStop.setImageResource(R.drawable.ic_round_play_arrow_24)
    }


    override fun onDetach() {
        super.onDetach()
        workTimer.cancel()
        restTimer.cancel()
        getReadyTimer.cancel()

        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun defineStartTimer(time: Long) {
        workTimer = object : CountDownTimer(time, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvStatus.text = "Go!"
                binding.tvTimerWork.text = (millisUntilFinished / 1000).toString()
                workTimeLeft = millisUntilFinished

                if (millisUntilFinished < 3000) {
                    mediaPlayer?.start()
                }
            }

            override fun onFinish() {
                sets--
                binding.tvTimerSets.text = "Sets: $sets"

                if (sets > 0) restTimer.start()
                if (sets == 0 || sets < 0) {
                    Toast.makeText(requireContext(), "Good Job!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }

                binding.fabStartStop.hide()
            }
        }
    }

    private fun defineRestTimer() {
        restTimer = object : CountDownTimer(restTime, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvStatus.text = "Rest."
                binding.tvTimerWork.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                defineStartTimer(workTime)
                workTimer.start()
                binding.fabStartStop.show()
            }

        }
    }

    private fun defineGetReadyTimer() {
        getReadyTimer = object : CountDownTimer(GET_READY_TIME, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvStatus.text = "Get Ready!"
                binding.tvTimerWork.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                //the get ready timer finishes, work timer begins
                workTimer.start()
                binding.fabStartStop.show()
            }
        }
    }
}