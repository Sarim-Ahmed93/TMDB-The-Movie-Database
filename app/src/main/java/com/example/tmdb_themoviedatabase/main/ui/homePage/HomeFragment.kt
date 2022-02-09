package com.example.tmdb_themoviedatabase.main.ui.homePage

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tmdb_themoviedatabase.R
import com.example.tmdb_themoviedatabase.main.backend.data.MovieItem
import com.example.tmdb_themoviedatabase.main.common.SquaredImageView
import com.example.tmdb_themoviedatabase.main.common.doNavigate
import com.example.tmdb_themoviedatabase.main.ui.BaseFragment
import kotlinx.android.synthetic.main.home_fragment.*
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.nav_graph)
    private val TAG = "HomeFragment"
    private lateinit var gridViewAdapter : GridViewAdapter
    private lateinit var searchListViewAdapter: SearchListViewAdapter

    companion object {
        fun newInstance() = HomeFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).let {
            contentView.addView(inflater.inflate(R.layout.home_fragment, container, false))
            ready = true
            it

        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        homeViewModel.dataReady.observe(viewLifecycleOwner) { dataReady ->

            var arrayList = ArrayList<MovieItem>()
            homeViewModel.movies.value!!?.forEach {it-> arrayList?.add(it) }
            //set grid View Adapter
            gridViewAdapter = GridViewAdapter(requireContext(),arrayList)
            movieGrid.adapter = gridViewAdapter
            gridViewAdapter.notifyDataSetChanged()

        }

        //set listview adapter
        searchListViewAdapter = SearchListViewAdapter(requireContext())
        listviewSearch.adapter = searchListViewAdapter
        listviewSearch.visibility = View.GONE

        movieSearchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!!.length>0) {listviewSearch.visibility = View.VISIBLE}
                else {listviewSearch.visibility = View.GONE}
                val text: String? = newText
                searchListViewAdapter.filter(text!!)
                searchListViewAdapter.notifyDataSetChanged()
                return false
            }
        })




    }

    private inner class GridViewAdapter(context : Context , arrayList: ArrayList<MovieItem>):  BaseAdapter()
    {
        //lateinit var imageview: SquaredImageView
         var colors = arrayOf(Color.RED, Color.BLUE, Color.DKGRAY, Color.BLACK, Color.GRAY, Color.MAGENTA)
        private var arraylist: ArrayList<MovieItem> = arrayList
        var mContext: Context
        lateinit var inflater: LayoutInflater


        init {
            Log.d(TAG,"gridview  adapter called")
            mContext = context
            inflater = LayoutInflater.from(mContext)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            Log.d(TAG, "in grid view adapter")

            var convertView = convertView
            var holder: ViewHolder? = null

            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                convertView = inflater.inflate(R.layout.movie_grid_layout, parent, false)
                holder = ViewHolder()
                convertView?.setTag(holder);

            }
            else
            {
                holder = convertView.tag as ViewHolder

            }
            holder.imageview = convertView!!.findViewById(R.id.posterView) as SquaredImageView
            holder.textViewName = convertView!!.findViewById(R.id.textViewName) as TextView

            var item =arraylist?.get(position)


            var url = item?.poster_path

            Log.d(TAG, "url : $url")
            try {

                Glide.with(requireActivity())
                    .load(url).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop()
                    .into(holder.imageview)
                holder.textViewName!!.setText(item?.title)
                holder.textViewName!!.setBackgroundColor(colors.random())

                convertView.setOnClickListener {
                    Toast.makeText(requireContext(), "view click ${item.id}", Toast.LENGTH_SHORT).show()

                    findNavController().doNavigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment2(item.id))
                }

            } catch (e: Exception) {
                Log.d(TAG, e.localizedMessage)
                Log.d(TAG, e.printStackTrace().toString())
            } finally {
                Log.d(TAG, "final")

            }


            return convertView


        }



        override fun getItemId(position: Int): Long {
            return position.toLong()
        }


        override fun getCount(): Int {
            if(arraylist == null)
                return 0
            else
                return arraylist!!.size
        }

        override fun getItem(position: Int): Any? {
            return arraylist?.get(position)
        }
         inner class ViewHolder {
            lateinit var imageview: SquaredImageView
            var textViewName: TextView? = null
        }
        fun filter(movieName: String) {
            var movieName = movieName
            movieName = movieName.toLowerCase(Locale.getDefault())
            try {
                arraylist!!.clear()
            } catch (e: Exception) {
            } finally {
            }
            Log.d(TAG,"in filter function")

            if (movieName.length == 0) {
                homeViewModel.movies.value!!?.forEach {it-> arraylist?.add(it) }
                Log.d(TAG,"filtered name : ${arraylist.toString()}")

            } else {
                for (movie in homeViewModel.movies.value!!) {
                    if (movie.title.equals(movieName,true)) {
                        arraylist!!.add(movie)

                        Log.d(TAG,"filtered name : ${movie.title}")
                    }
                }
                Log.d(TAG,"filtered name : no  movie")

            }
            notifyDataSetChanged()
        }

    }

    private inner class SearchListViewAdapter(context : Context) : BaseAdapter() {
        // Declare Variables
        var mContext: Context
        lateinit var inflater: LayoutInflater
        private var arraylist: ArrayList<String>? = null

        init {
            arraylist = ArrayList<String>()
            homeViewModel.movies.value?.forEach {it-> arraylist?.add(it.title) }
            mContext = context
            inflater = LayoutInflater.from(mContext)
        }

        inner class ViewHolder {
            var name: TextView? = null
        }
        override fun getCount(): Int {
            if(arraylist == null)
                return 0
            else
                return arraylist!!.size
        }

        override fun getItem(position: Int): Any? {
            return arraylist?.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            var view = view
            val holder: ViewHolder?
            if (view == null) {
                holder = ViewHolder()
                view =  layoutInflater.inflate(R.layout.search_list_view_item, parent, false)
                // Locate the TextViews in listview_item.xml
                holder.name = view.findViewById<View>(R.id.name) as TextView
                view.tag = holder
            } else {
                holder = view.tag as ViewHolder
            }
            // Set the results into TextViews
            holder.name?.setText("${arraylist?.get(position)}")

            holder.name?.setOnClickListener {
                gridViewAdapter.filter(arraylist?.get(position)!!)
                listviewSearch.visibility = View.GONE
            }
            return view!!
        }

        // Filter list Class
        fun filter(charText: String) {
            var charText = charText
            charText = charText.toLowerCase(Locale.getDefault())
            try {
                arraylist!!.clear()
            } catch (e: Exception) {
            } finally {
            }
            Log.d(TAG,"in filter function")

            if (charText.length == 0) {
                homeViewModel.movies.value!!?.forEach {it-> arraylist?.add(it.title) }
                Log.d(TAG,"filtered name : ${arraylist.toString()}")
                gridViewAdapter.filter("")

            } else {
                if(homeViewModel.movies.value!!!!.isNullOrEmpty()) {Log.d(TAG,"movie list is empty or null")}
                for (movie in homeViewModel.movies.value!!) {
                    if (movie.title.toLowerCase(Locale.getDefault()).contains(charText)) {
                        arraylist!!.add(movie.title)
                        Log.d(TAG,"filtered name : ${movie.title}")
                    }
                }
                Log.d(TAG,"filtered name : no  movie")

            }
            notifyDataSetChanged()
        }

    }
}