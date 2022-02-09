package com.example.tmdb_themoviedatabase.main.ui.detailPage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.tmdb_themoviedatabase.R
import com.example.tmdb_themoviedatabase.main.ui.BaseFragment
import kotlinx.android.synthetic.main.app_bar_layout.*


class DetailFragment : BaseFragment() {
    private val args : DetailFragmentArgs by navArgs()
    private val detailViewModel: DetailViewModel by navGraphViewModels(R.id.nav_graph)

    companion object {
        fun newInstance() = DetailFragment()
    }

    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).let {
            contentView.addView(inflater.inflate(R.layout.home_fragment, container, false))
            ready = true
            it

        }    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Toast.makeText(requireContext(), "id received : ${args.id}", Toast.LENGTH_SHORT).show()


        // TODO: Use the ViewModel
    }

}