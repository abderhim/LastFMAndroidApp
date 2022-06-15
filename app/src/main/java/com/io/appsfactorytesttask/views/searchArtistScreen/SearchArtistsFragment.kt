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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.io.appsfactorytesttask.R
import com.io.appsfactorytesttask.data.entities.Artist
import com.io.appsfactorytesttask.databinding.FragmentSearchScreenBinding
import com.io.appsfactorytesttask.views.adapters.ArtistsAdapter
import com.io.appsfactorytesttask.viewModels.MainVM
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.GridLayoutManager
import com.io.appsfactorytesttask.data.network.ApiState
import com.io.appsfactorytesttask.utilities.createLoadingPopup
import com.io.appsfactorytesttask.utilities.verifyArtistName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class SearchArtistsFragment : Fragment() {
    private var binding: FragmentSearchScreenBinding? = null
    private val searchScreenBinding get() = binding!!
    private lateinit var artistsAdapter: ArtistsAdapter
    private var artistList = mutableListOf<Artist>()
    private lateinit var mainVM: MainVM
    private lateinit var artistsRecyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchScreenBinding.inflate(inflater, container, false)
        artistsAdapter = ArtistsAdapter(requireContext(), activity as AppCompatActivity)
        mainVM = ViewModelProvider(this).get(MainVM::class.java)
        initRecyclerView()
        searchScreenBinding.searchButton.setOnClickListener {
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
        artistsAdapter.differ.submitList(artistList)

    }




    private fun searchArtists(artist: String, context: Context) {

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                mainVM.searchArtist(artist, context)
                val loading = createLoadingPopup(context, getString(R.string.loading_popup_text))
                mainVM.flow.collect { it ->
                    when (it) {
                        is ApiState.Loading -> {
                            loading.show()
                        }
                        is ApiState.Failure -> {
                            loading.dismiss()
                            searchScreenBinding.noArtistsMessage.visibility = View.VISIBLE
                            searchScreenBinding.artistsRecyclerView.visibility = View.GONE
                            searchScreenBinding.noArtistsMessage.text = getString(R.string.failure_response_message)

                        }
                        is ApiState.SuccessSearchingArtists -> {
                            loading.dismiss()
                            searchScreenBinding.noArtistsMessage.visibility = View.GONE
                            searchScreenBinding.artistsRecyclerView.visibility = View.VISIBLE
                            artistList = it.data?.artist as MutableList<Artist>
                            artistsAdapter.differ.submitList(artistList)
                            if(artistList.isEmpty()){
                                searchScreenBinding.artistsRecyclerView.visibility = View.GONE
                                searchScreenBinding.noArtistsMessage.visibility = View.VISIBLE
                            }
                        }
                        is ApiState.Empty -> {
                            searchScreenBinding.artistsRecyclerView.visibility = View.GONE
                            searchScreenBinding.noArtistsMessage.visibility = View.VISIBLE
                            loading.dismiss()
                        }
                    }
                }
            }
        }

    }

}