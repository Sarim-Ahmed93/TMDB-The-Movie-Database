package com.example.tmdb_themoviedatabase.main.ui.detailPage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tmdb_themoviedatabase.R
import com.example.tmdb_themoviedatabase.main.backend.data.MovieItem
import com.example.tmdb_themoviedatabase.main.ui.BaseFragment
import kotlinx.android.synthetic.main.app_bar_layout.*
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.home_fragment.*


class DetailFragment : BaseFragment() {
    private val args : DetailFragmentArgs by navArgs()
    private val detailViewModel: DetailViewModel by navGraphViewModels(R.id.nav_graph)
    private lateinit var movieItem: MovieItem
    private val TAG = "DetailFragment"
    companion object {
        fun newInstance() = DetailFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).let {
            contentView.addView(inflater.inflate(R.layout.detail_fragment, container, false))

            ready = true
            it

        }    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

            movieItem = detailViewModel.getMovieDetail(args.id)
            setTitle(movieItem.title!!)
            Log.d(TAG, "in grid view adapter")
            Log.d(TAG,"id received : ${args.id}")

        // TODO: Use the ViewModel

        initiate()
    }

    fun initiate()
    {
        Glide.with(requireActivity()).load(movieItem.backdrop_path).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageViewBackDrop)
        textViewTitle.setText(movieItem.original_title)
        textViewReleaseDate.setText("${resources.getString(R.string.release_date)} ${movieItem.release_date}")
    }

}