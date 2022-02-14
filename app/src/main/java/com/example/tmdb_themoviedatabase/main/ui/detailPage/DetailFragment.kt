package com.example.tmdb_themoviedatabase.main.ui.detailPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tmdb_themoviedatabase.R
import com.example.tmdb_themoviedatabase.main.backend.data.Cast
import com.example.tmdb_themoviedatabase.main.backend.data.MovieItem
import com.example.tmdb_themoviedatabase.main.common.RoundedImageView
import com.example.tmdb_themoviedatabase.main.common.SquaredImageView
import com.example.tmdb_themoviedatabase.main.ui.BaseFragment
import kotlinx.android.synthetic.main.detail_fragment.*


class DetailFragment : BaseFragment() {
    private val args : DetailFragmentArgs by navArgs()
    private val detailViewModel: DetailViewModel by navGraphViewModels(R.id.nav_graph)
    private lateinit var movieItem: MovieItem
    private var castAdapter : CastAdapter? = null
    private var genreAdapter : GenreAdapter? = null
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
            Log.d(TAG, "id received : ${args.id}")

        // TODO: Use the ViewModel

        initiate()
    }

    //initialize UI
    fun initiate()
    {
        detailViewModel.getCast(movieItem.id!!)
        Glide.with(requireActivity()).load(movieItem.backdrop_path).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageViewBackDrop)
        textViewTitle.setText(movieItem.original_title)
        textViewReleaseDate.setText("${resources.getString(R.string.release_date)} ${movieItem.release_date}")
        textViewDetails.setText(movieItem.overview)
        Log.d(TAG,"genres : "+detailViewModel.getGenres(movieItem))

        //call for adapter for genres
        genreAdapter = GenreAdapter(detailViewModel.getGenres(movieItem))
        recyclerViewGenres.adapter = genreAdapter
        recyclerViewGenres.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        genreAdapter!!.notifyDataSetChanged()

        //we have to wait untill it fetches the cast
        detailViewModel.castReady.observe(viewLifecycleOwner)
        { ready->
            if (ready)
            {
                castAdapter = CastAdapter(detailViewModel.cast.value!!)
                recyclerViewCast.adapter = castAdapter
                recyclerViewCast.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                castAdapter!!.notifyDataSetChanged()
            }

        }
    }



    //////Adapters for Cast and Genres Below
    inner class CastAdapter(list: List<Cast>) : RecyclerView.Adapter<CastAdapter.ViewHolder>() {
        // ... ViewHolder class and its constructor as per above
        var list : List<Cast>

        // Creating a viewHolder
        @NonNull
        override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
            // Inflate the layout
            val contactView: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.cast_item_layout, parent, false)

            // Return a new holder instance
            return ViewHolder(contactView)
        }

        // Assigning respective data for the views based on the position of the current item
        override fun onBindViewHolder(@NonNull holder: CastAdapter.ViewHolder, position: Int) {
            // Get the Subject based on the current position
            val currentItem: Cast = list[position]

            // Setting views with the corresponding data
            val imageView: SquaredImageView = holder.profileImageView
            //imageView.setImageResource(currentItem.getImageId())
            Glide.with(requireActivity()).load(currentItem.profile_path).diskCacheStrategy(DiskCacheStrategy.NONE).fitCenter().into(imageView)
            val textViewName: TextView = holder.textViewName
            textViewName.setText(currentItem.name)
            val textViewCharacter: TextView = holder.textViewCharacter
            textViewCharacter.setText(currentItem.character)
        }

        // Indicating how long your data is
        override fun getItemCount(): Int {
            if(list.size > 0)
                return list.size
            else
                return 0
        }

        // Constructor
        init {
            if(list != null)
            {
                this.list = list
            }
            else
            {
                this.list = emptyList()
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            lateinit var profileImageView: SquaredImageView
            lateinit var textViewName: TextView
            lateinit var textViewCharacter: TextView

            // Constructor - accepts entire row item
            init {

                // Find each view by id you set up in the list_item.xml
                profileImageView = itemView.findViewById(R.id.imageViewProfileImage)
                textViewName = itemView.findViewById(R.id.textViewName)
                textViewCharacter = itemView.findViewById(R.id.textViewCharacter)
            }
        }
    }
    inner class GenreAdapter(list: ArrayList<String>?) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {
        // ... ViewHolder class and its constructor as per above
        var list = ArrayList<String>()

        // Creating a viewHolder
        @NonNull
        override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
            // Inflate the layout
            val contactView: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.genre_item_layout, parent, false)

            // Return a new holder instance
            return ViewHolder(contactView)
        }

        // Assigning respective data for the views based on the position of the current item
        override fun onBindViewHolder(@NonNull holder: GenreAdapter.ViewHolder, position: Int) {
            // Get the Subject based on the current position
            val currentItem = list[position]

            // Setting views with the corresponding data
            val textViewGenre: TextView = holder.textViewGenre
            textViewGenre.setText(currentItem)
        }

        // Indicating how long your data is
        override fun getItemCount(): Int {
            if(list.size > 0)
                return list.size
            else
                return 0
        }

        // Constructor
        init {
            if (list != null) {
                this.list = list
            }
            else
            {
                this.list = ArrayList()
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            lateinit var textViewGenre: TextView
            init {
                // Find each view by id you set up in the list_item.xml
                textViewGenre = itemView.findViewById(R.id.textViewGenre)
            }
        }
    }

}