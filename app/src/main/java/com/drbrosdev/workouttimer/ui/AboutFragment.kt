package com.drbrosdev.workouttimer.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.drbrosdev.workouttimer.BuildConfig
import com.drbrosdev.workouttimer.R
import com.drbrosdev.workouttimer.databinding.FragmentAboutBinding
import com.google.android.material.transition.MaterialSharedAxis

class AboutFragment: Fragment(R.layout.fragment_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAboutBinding.bind(view)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

        binding.apply {
            val versionName = BuildConfig.VERSION_NAME
            tvVersion.text = "Version $versionName"
        }
    }
}