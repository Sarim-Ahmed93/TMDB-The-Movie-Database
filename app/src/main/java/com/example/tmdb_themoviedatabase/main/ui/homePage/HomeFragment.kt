package com.example.tmdb_themoviedatabase.main.ui.homePage

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tmdb_themoviedatabase.R
import com.example.tmdb_themoviedatabase.main.common.SquaredImageView
import com.example.tmdb_themoviedatabase.main.ui.BaseFragment
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.movie_grid_layout.*


class HomeFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.nav_graph)
    private val TAG = "HomeFragment"
    private lateinit var gridViewAdapter : GridViewAdapter

    companion object {
        fun newInstance() = HomeFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).let {

            contentView.addView(inflater.inflate(R.layout.home_fragment, container, false))
            it

        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        gridViewAdapter = GridViewAdapter()

        movieGrid.adapter = GridViewAdapter()
        gridViewAdapter.notifyDataSetChanged()




    }

    private inner class GridViewAdapter:  BaseAdapter()
    {
        //lateinit var imageview: SquaredImageView
         var colors = arrayOf(Color.RED, Color.BLUE, Color.DKGRAY, Color.BLACK, Color.GRAY, Color.MAGENTA)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            Log.d(TAG, "in grid view adapter")

            var convertView = convertView
            var holder: ViewHolder? = null

            if (convertView == null) {
                // if it's not recycled, initialize some attributes

                convertView = layoutInflater.inflate(R.layout.movie_grid_layout, parent, false)
                holder = ViewHolder()


                convertView.setTag(holder);

                /*imageview = SquaredImageView(requireContext())
                imageview.setPadding(4, 4, 4, 4);*/


            }
            else
            {
                holder = convertView.tag as ViewHolder
                //imageview = convertView as SquaredImageView

            }
           holder.imageview = convertView!!.findViewById(R.id.posterView) as SquaredImageView
            holder.textViewName = convertView!!.findViewById(R.id.textViewName) as TextView
            //Log.d("picasso",""+arraylist_templates?.get(position)?.url)

            var item = homeViewModel.movies.value?.get(position)
            var url = item?.poster_path

            Log.d(TAG, "url : $url")
            try {

                Glide.with(requireActivity())
                    .load(url).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop()
                    .into(holder.imageview)
                holder.textViewName!!.setText(item?.title)
                holder.textViewName!!.setBackgroundColor(colors.random())

                /*        .listener(object: RequestListener<String, GlideDrawable> {
                            override fun onException(e: java.lang.Exception?, model: String?, target: com.bumptech.glide.request.target.Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                                progressBar.setVisibility(View.GONE)
                                return false;                    }

                            override fun onResourceReady(resource: GlideDrawable?, model: String?, target: com.bumptech.glide.request.target.Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                progressBar.setVisibility(View.GONE)
                                return false;                    }


                        }).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().crossFade()*/




            } catch (e: Exception) {
                Log.d(TAG, e.localizedMessage)
                Log.d(TAG, e.printStackTrace().toString())
            } finally {
                Log.d(TAG, "final")

            }


            return convertView


        }

        override fun getItem(position: Int): Any? {
            return position

        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }


        override fun getCount(): Int {
            return homeViewModel.movies.value!!.size
        }
         inner class ViewHolder {
            lateinit var imageview: SquaredImageView
            var textViewName: TextView? = null
        }

    }
}