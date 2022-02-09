package com.example.tmdb_themoviedatabase.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tmdb_themoviedatabase.R
import com.example.tmdb_themoviedatabase.main.activities.MainActivity

open class BaseFragment : Fragment(){

    // set this to another value in init of descendant
    var softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
    // siblings should place their content into contentView
    protected lateinit var contentView: ViewGroup

    protected var ready = false
        set(value) {
            field = value
            if(value) {
                pBar.visibility = View.GONE
                contentView.visibility = View.VISIBLE
            } else {
                pBar.visibility = View.VISIBLE
                contentView.visibility = View.VISIBLE
            }
        }

    private lateinit var pBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.setSoftInputMode(softInputMode)

        return inflater.inflate(R.layout.base_fragment, container, false).let {
            contentView = it.findViewById(R.id.contentLayout)
            pBar = it.findViewById(R.id.progressBar)
            ready = false
            (activity as MainActivity).supportActionBar?.subtitle = ""



            it
        }
    }

    fun setTitle(title: String, subTitle: String = "") {
        (activity as MainActivity).setupAppBar(title, subTitle)
    }
}