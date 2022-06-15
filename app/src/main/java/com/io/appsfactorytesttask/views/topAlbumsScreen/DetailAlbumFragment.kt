package com.io.appsfactorytesttask.views.topAlbumsScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.io.appsfactorytesttask.R
import com.io.appsfactorytesttask.databinding.FragmentDetailAlbumBinding
import com.io.appsfactorytesttask.viewModels.MainVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.io.appsfactorytesttask.data.entities.*
import com.io.appsfactorytesttask.data.network.ApiState
import com.io.appsfactorytesttask.utilities.*
import com.io.appsfactorytesttask.views.adapters.TrackAdapter
import com.io.appsfactorytesttask.views.mainScreen.MainScreenFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class DetailAlbumFragment : Fragment() {
    private var binding: FragmentDetailAlbumBinding? = null
    private val detailAlbumBinding get() = binding!!
    private lateinit var mainVM: MainVM
    private var tracksList = mutableListOf<Track>()
    private lateinit var tracksRecyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailAlbumBinding.inflate(inflater, container, false)
        mainVM = ViewModelProvider(this).get(MainVM::class.java)
        trackAdapter = TrackAdapter(
            requireContext(),
            activity as AppCompatActivity
        )

        initRecyclerView()

        val arguments = arguments
        val artistName = arguments?.getString(getString(R.string.artist_name))
        val albumName = arguments?.getString(getString(R.string.album_name))
        val albumImage = arguments?.getString(getString(R.string.album_image))
        val comingFrom = arguments?.getString(getString(R.string.coming_from))


        if (artistName != null) {
            val bitmap = stringToBitMap(albumImage)
            detailAlbumBinding.albumName.text =
                context?.getString(R.string.album_name_detail, albumName)
            detailAlbumBinding.artistName.text =
                context?.getString(R.string.artist_name_detail, artistName)
            detailAlbumBinding.albumImage.setImageBitmap(bitmap)
        }

        if (comingFrom == getString(R.string.top_albums_fragment)) {
            getAlbumTracksFromApi(albumName.toString(), artistName.toString())
            detailAlbumBinding.topAlbumButton.text = getString(R.string.save_album)
            detailAlbumBinding.topAlbumButton.setOnClickListener {
                saveAlbum(albumName, artistName, albumImage)
                detailAlbumBinding.seeMyAlbums.visibility=View.VISIBLE
            }
        } else {
            detailAlbumBinding.seeMyAlbums.visibility=View.GONE
            getAlbumTracksFromDB(albumName.toString(), artistName.toString())
            detailAlbumBinding.topAlbumButton.text = getString(R.string.delete_album)
            detailAlbumBinding.topAlbumButton.setOnClickListener {
                deleteAlbum(albumName, artistName, albumImage)
                deleteAlbumTracks(albumName, artistName)
            }
        }


        detailAlbumBinding.seeMyAlbums.setOnClickListener {
            showFragment(activity as AppCompatActivity, MainScreenFragment())
        }


        return detailAlbumBinding.root
    }


    private fun saveAlbum(albumName: String?, artistName: String?, albumImage: String?) {
        val album = SavedAlbum(albumName, artistName, albumImage)
        GlobalScope.launch(Dispatchers.Main) {

            if (mainVM.getAlbumsByName(album.albumName.toString(), album.artistName.toString()).isNotEmpty()) {
                context?.let { it1 ->
                    createErrorPopup(
                        it1,
                        getString(R.string.album_already_added)
                    ).show()

                    detailAlbumBinding.topAlbumButton.visibility=View.GONE
                }
            } else {
                mainVM.addAlbum(album)
                saveAlbumTracks(albumName, artistName)
                detailAlbumBinding.topAlbumButton.visibility = View.GONE
                Toast.makeText(
                    activity,
                    getString(R.string.album_added),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }

    }



    private fun deleteAlbum(albumName: String?, artistName: String?, albumImage: String?) {
        val album = SavedAlbum(albumName, artistName, albumImage)
        GlobalScope.launch(Dispatchers.Main) {
            mainVM.deleteAlbum(album.albumName.toString())
            Toast.makeText(activity, getString(R.string.album_deleted), Toast.LENGTH_SHORT)
                .show()
            activity?.supportFragmentManager?.popBackStack()
        }
    }


    private fun saveAlbumTracks(albumName: String?, artistName: String?) {
        if (tracksList.isNotEmpty()) {
            tracksList.forEach { track ->
                val savedTrack = SavedTrack(track.name, albumName, artistName)
                GlobalScope.launch(Dispatchers.Main) {
                    mainVM.addTrack(savedTrack)
                }
            }

        }


    }


    private fun deleteAlbumTracks(albumName: String?, artistName: String?) {


        GlobalScope.launch(Dispatchers.Main) {
            val listTracksFromDB = mainVM.getAlbumTracks(albumName.toString(), artistName.toString())
            listTracksFromDB.forEach { savedTrack ->

                    mainVM.deleteAlbumTracks(
                        savedTrack.trackAlbum.toString(),
                        savedTrack.trackArtist.toString()
                    )

                }



        }
    }


    private fun getAlbumTracksFromDB(albumName: String, artistName: String) {
        GlobalScope.launch(Dispatchers.Main) {
        tracksList.clear()

        val listTracksFromDB = mainVM.getAlbumTracks(albumName, artistName)


                    listTracksFromDB.forEach { savedTrack ->

                        val track = Track(savedTrack.trackName)
                        tracksList.add(track)
                    }

                    if(tracksList.isNullOrEmpty()){
                        detailAlbumBinding.albumTracksTitle.text = getString(R.string.no_trucks)
                    }
                    else {
                        trackAdapter =
                            TrackAdapter(
                                requireContext(),
                                activity as AppCompatActivity,
                            )
                        tracksRecyclerView.adapter = trackAdapter
                        trackAdapter.differ.submitList(tracksList)
                    }


                }


    }


    private fun getAlbumTracksFromApi(album: String, artist: String) {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                mainVM.getAlbumInfo(album, artist, context!!)
                val loading = createLoadingPopup(context!!, getString(R.string.loading_popup_text))
                mainVM.flow.collect { it ->
                    when (it) {
                        is ApiState.Loading -> {
                            loading.show()
                        }
                        is ApiState.Failure -> {
                            loading.dismiss()
                            detailAlbumBinding.albumTracksTitle.text = getString(R.string.no_trucks)
                        }
                        is ApiState.SuccessGettingAlbumInfo -> {
                            loading.dismiss()
                            val tracksListResponse = it.data?.trackList
                            if (!tracksListResponse.isNullOrEmpty()) {
                                tracksList = tracksListResponse as MutableList<Track>
                            } else {
                                detailAlbumBinding.albumTracksTitle.text =
                                    getString(R.string.no_trucks)
                            }
                            trackAdapter.differ.submitList(tracksList)


                        }
                        is ApiState.Empty -> {
                            detailAlbumBinding.albumTracksTitle.text = getString(R.string.no_trucks)
                            loading.dismiss()
                        }
                    }
                }
            }
        }

    }


    private fun initRecyclerView() {
        tracksRecyclerView = detailAlbumBinding.tracksRecyclerView
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        tracksRecyclerView.layoutManager = mLayoutManager
        tracksRecyclerView.itemAnimator = DefaultItemAnimator()
        tracksRecyclerView.adapter = trackAdapter
        trackAdapter.differ.submitList(tracksList)
    }

}