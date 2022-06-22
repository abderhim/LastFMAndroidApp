package com.io.appsfactorytesttask.views.searchArtistScreen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.io.appsfactorytesttask.R
import com.io.appsfactorytesttask.data.entities.Artist
import com.io.appsfactorytesttask.databinding.FragmentSearchScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.GridLayoutManager
import com.io.appsfactorytesttask.data.network.ApiState
import com.io.appsfactorytesttask.viewModels.SearchArtistsVM
import com.io.appsfactorytesttask.views.adapters.ArtistsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.os.Parcelable
import com.io.appsfactorytesttask.utilities.*



@AndroidEntryPoint
class SearchArtistsFragment : Fragment() {
    private var binding: FragmentSearchScreenBinding? = null
    private val searchScreenBinding get() = binding!!
    private lateinit var artistsAdapter: ArtistsAdapter
    private var artistList = mutableListOf<Artist>()
    private lateinit var searchArtistsVM: SearchArtistsVM
    private lateinit var artistsRecyclerView: RecyclerView
    private var noArtists =false
    private var noInternet =false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchScreenBinding.inflate(inflater, container, false)
        if(savedInstanceState!=null){
            hundleNoArtistsOrInternetMessagesState()
        }
        artistsAdapter = ArtistsAdapter(requireContext(), activity as AppCompatActivity)
        searchArtistsVM = ViewModelProvider(this)[SearchArtistsVM::class.java]
        initRecyclerView()
        searchScreenBinding.searchButton.setOnClickListener {
            searchScreenBinding.searchEditText.clearFocus()
            if (verifyArtistName(searchScreenBinding.searchEditText.text.toString())) {
                context?.let { it1 ->
                    searchArtists(
                        searchScreenBinding.searchEditText.text.toString(),
                        it1
                    )
                }
            } else {
                Toast.makeText(
                    activity,
                    getString(R.string.error_artist_search_empty),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return searchScreenBinding.root

    }




    private fun initRecyclerView() {
        artistsRecyclerView = searchScreenBinding.artistsRecyclerView
        val layoutManager = GridLayoutManager(context, 3)
        artistsRecyclerView.layoutManager = layoutManager
        artistsRecyclerView.itemAnimator = DefaultItemAnimator()
        artistsRecyclerView.adapter = artistsAdapter
        artistsAdapter.submitList(artistList)

    }




    private fun searchArtists(artist: String, context: Context) {

        lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.Main) {

                searchArtistsVM.searchArtist(artist, context)
                val loading = createLoadingPopup(context, getString(R.string.loading_popup_text))
                searchArtistsVM.flow.collect { it ->
                    when (it) {
                        is ApiState.Loading -> {
                            loading.show()
                        }
                        is ApiState.Failure -> {
                            loading.dismiss()
                            searchScreenBinding.noArtistsMessage.visibility = View.VISIBLE
                            searchScreenBinding.artistsRecyclerView.visibility = View.GONE
                            searchScreenBinding.noArtistsMessage.text = getString(R.string.failure_response_message)
                            noInternet=true
                            artistList.clear()
                        }
                        is ApiState.SuccessSearchingArtists -> {
                            loading.dismiss()
                            noArtists=false
                            noInternet=false
                            searchScreenBinding.noArtistsMessage.visibility = View.GONE
                            searchScreenBinding.artistsRecyclerView.visibility = View.VISIBLE
                            artistList = it.data?.artist as MutableList<Artist>
                            artistsAdapter.submitList(artistList)
                            if(artistList.isEmpty()){
                                searchScreenBinding.artistsRecyclerView.visibility = View.GONE
                                searchScreenBinding.noArtistsMessage.visibility = View.VISIBLE
                                noArtists=true
                            }
                        }
                        is ApiState.Empty -> {
                            searchScreenBinding.artistsRecyclerView.visibility = View.GONE
                            searchScreenBinding.noArtistsMessage.visibility = View.VISIBLE
                            noArtists=true
                            loading.dismiss()
                        }
                    }

                    hundleNoArtistsOrInternetMessagesState()
                }


            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if(savedInstanceState!=null  ){
          val savedArtistsList =  savedInstanceState?.getSerializable(ARTIST_LIST_STATE)
            if(savedArtistsList!=null){ artistList = savedArtistsList as MutableList<Artist> }
            noArtists = savedInstanceState?.getBoolean(NO_ARTISTS)
            noInternet = savedInstanceState?.getBoolean(NO_INTERNET)
        }
        super.onCreate(savedInstanceState)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (noArtists) {
            outState.putBoolean(NO_ARTISTS, true)
        }
        if (noInternet) {
            outState.putBoolean(NO_INTERNET, true)
        }
        if(!artistList.isNullOrEmpty()){
            outState.putSerializable(ARTIST_LIST_STATE, artistList as ArrayList<out Parcelable?>?)
        }
    }


    private fun hundleNoArtistsOrInternetMessagesState(){
        if (noArtists) {
            searchScreenBinding.noArtistsMessage.visibility = View.VISIBLE
            searchScreenBinding.noArtistsMessage.text = getString(R.string.no_artists_text)
        }
        if(noInternet){
            searchScreenBinding.noArtistsMessage.visibility = View.VISIBLE
            searchScreenBinding.noArtistsMessage.text = getString(R.string.failure_response_message)
        }
    }
}

