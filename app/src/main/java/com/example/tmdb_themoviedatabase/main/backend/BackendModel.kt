package com.example.tmdb_themoviedatabase.main.backend

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import com.example.tmdb_themoviedatabase.main.backend.data.Genre
import com.example.tmdb_themoviedatabase.main.backend.data.MovieItem
import com.example.tmdb_themoviedatabase.main.backend.data.MovieList
import com.example.tmdb_themoviedatabase.main.backend.rest.GenreApiService
import com.example.tmdb_themoviedatabase.main.backend.rest.MovieListApiService
import com.example.tmdb_themoviedatabase.main.common.Constants
import com.example.tmdb_themoviedatabase.main.common.EnumAsOrdinalToStringConverterFactory
import com.example.tmdb_themoviedatabase.main.common.Keys
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit


class BackendModel : LifecycleOwner{
    private var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private var builder: Retrofit.Builder = Retrofit.Builder()
    private var retrofit: Retrofit
    private var eventListeners = mutableListOf<WeakReference<BackendEventListener>>()
    private var retrofitCalls = mutableMapOf<Long, Call<*>>() // holds references to retrofit calls
    private lateinit var movieListApiService: MovieListApiService
    private lateinit var genreApiService : GenreApiService

    private val TAG = "BackendModel"

    val movies: LiveData<List<MovieItem>>
        get() = _movies
    val genres: LiveData<List<Genre>>
        get() = _genres

    private var _movies = MutableLiveData<List<MovieItem>>(listOf())
    private var _genres = MutableLiveData<List<Genre>>(listOf())

    interface BackendEventListener {
        fun onStartLoadingMenu() {}
        fun onMenuLoaded(successful: Boolean) {}
        fun onCommitExecuted(successful: Boolean, ticketId: Int) {}
        fun onUpdateTicketById(successful: Boolean, ticketId: Int) {}
        fun onUpdateTicketsForTable(successful: Boolean, tableId: Int) {}
        fun onConnectionLost() {}
        fun onConnectionEstablished() {}
    }

    init {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED

        val httpClient = RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
        httpClient.readTimeout(10000, TimeUnit.MILLISECONDS)
        httpClient.connectTimeout(10000, TimeUnit.MILLISECONDS)

        // add specific header for app id
            httpClient.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    val request = chain.request().newBuilder()
                        .build()
                    return chain.proceed(request)
                }

            })


        // --- logging -
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY   // log everything: request, response, headers, bodies
        httpClient.addInterceptor(logging)  // logging should be the last interceptor

        val gsonBuilder: Gson = GsonBuilder()
                .serializeNulls()
                .create()

        Log.d(TAG, "request generated")

        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(EnumAsOrdinalToStringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
                .baseUrl(Constants.baseURL)
                .client(httpClient.build())

        retrofit = builder.build()

        movieListApiService = retrofit.create(MovieListApiService::class.java)
        genreApiService = retrofit.create(GenreApiService::class.java)


        syncData()

    }
    override fun getLifecycle(): Lifecycle {
        TODO("Not yet implemented")
    }

    fun scrap()
    {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    fun addListener(listener: BackendEventListener) {
        eventListeners.add(WeakReference(listener))
    }
    fun removeListener(listener: BackendEventListener) {
        eventListeners.removeIf { it.get() == listener }
    }

    /**
     * Add an retrofit call to [retrofitCalls] and return an handle to it and clean up
     * executed or canceled calls.
     */
    private fun addAsyncOperationCall(call: Call<*>): AsyncBackendOperationHandle {
        trimAsyncOperationsStorage()
        return AsyncBackendOperationHandle().apply { retrofitCalls[uId] = call }
    }

    /**
     * can be used to cancel asynchronous backend-operations
     */
    fun cancelAsyncOperation(operationHandle: AsyncBackendOperationHandle) {
        retrofitCalls[operationHandle.uId]?.cancel()
        retrofitCalls.remove(operationHandle.uId)
    }

    /**
     * Removes all executed or canceled entries from retrofitCalls
     */
    private fun trimAsyncOperationsStorage() {
        retrofitCalls = retrofitCalls.filterValues { !it.isCanceled && !it.isExecuted } as MutableMap
    }

    /**
     * Fetches list of movies.
     */
    @SuppressLint("CheckResult")
    private fun fetchMovies(onGetMoviesList: ((movieList: MovieList, successful: Boolean, errorString: String) -> Unit)? = null) {
        movieListApiService.getList(1,Constants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        //Log.d(TAG, "backend movie list" + result.toString())
                        onGetMoviesList?.invoke(result, true, "")
                    },
                    { error ->
                        Log.d(TAG, "backend movie list error ${error.message}")

                        onGetMoviesList?.invoke(
                            MovieList(emptyList()),
                            false,
                            error.message ?: ""
                        )

                    }
                )
    }


    /**
     * Fetches list of Genres.
     */
    @SuppressLint("CheckResult")
    private fun fetchGenres(onGenressFetched: ((genresList: List<Genre>, successful: Boolean, errorString: String) -> Unit)? = null): AsyncBackendOperationHandle {

        val call = genreApiService.all(Constants.API_KEY)
        call.enqueue(object : Callback<List<Genre>> {
            override fun onResponse(call: Call<List<Genre>>, response: Response<List<Genre>>) {
                response.body().let { body ->
                    Log.d(TAG, "${response.message()}")
                    onGenressFetched?.invoke(body ?: emptyList(), response.isSuccessful, response.message())
                }
            }
            override fun onFailure(call: Call<List<Genre>>, t: Throwable) {
                if (call.isCanceled) {
                    Log.d(TAG, "call canceled")
                } else {
                    onGenressFetched?.invoke(emptyList(), false, t.toString())
                }
            }
        })

        return addAsyncOperationCall(call)
    }

    fun updateMovieList(onUpdated: ((successful: Boolean) -> Unit)? = null) {

        fetchMovies { movieList, successful, _ ->
            if (successful) {

                movieList.results.forEach { it->
                    it.poster_path = Constants.basePosterURL + it.poster_path
                    it.backdrop_path = Constants.baseBackdropURL + it.backdrop_path
                }

                _movies.value = movieList.results

                Log.d(TAG, "final data list : ${_movies.value.toString()}")

            }
            else {

            }
            onUpdated?.invoke(successful)
        }
        //}
    }

    fun updateGenreList(onUpdated: ((successful: Boolean) -> Unit)? = null) {

        fetchGenres { genresList, successful, errorString ->
            if (successful) {
                _genres.value = genresList
                Log.d(TAG, "final data list : ${_movies.value.toString()}")
            }
            else { }
            onUpdated?.invoke(successful)
        }
        //}
    }

    fun syncData()
    {
        Log.d(TAG, "syncData")
        updateMovieList()
        updateGenreList()
    }
}