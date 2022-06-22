package com.io.appsfactorytesttask.views.topAlbumsScreen

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.io.appsfactorytesttask.R
import com.io.appsfactorytesttask.databinding.FragmentDetailAlbumBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.io.appsfactorytesttask.data.entities.*
import com.io.appsfactorytesttask.data.network.ApiState
import com.io.appsfactorytesttask.utilities.*
import com.io.appsfactorytesttask.viewModels.DetailAlbumVM
import com.io.appsfactorytesttask.views.adapters.TrackAdapter
import com.io.appsfactorytesttask.views.mainScreen.MainScreenFragment
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class DetailAlbumFragment : Fragment() {
    private var binding: FragmentDetailAlbumBinding? = null
    private val detailAlbumBinding get() = binding!!
    private lateinit var detailAlbumVM: DetailAlbumVM
    private var tracksList = mutableListOf<Track>()
    private lateinit var tracksRecyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private var noTracks = false
    private var albumAlreadySaved = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailAlbumBinding.inflate(inflater, container, false)
        detailAlbumVM = ViewModelProvider(this)[DetailAlbumVM::class.java]
        trackAdapter = TrackAdapter()
        if (albumAlreadySaved) {
            detailAlbumBinding.seeMyAlbums.visibility = View.VISIBLE
            detailAlbumBinding.topAlbumButton.visibility = View.GONE
        }
        initRecyclerView()

        val arguments = arguments
        val artistName = arguments?.getString(getString(R.string.artist_name))
        val albumName = arguments?.getString(getString(R.string.album_name))
        val albumImage = arguments?.getString(getString(R.string.album_image))
        val comingFrom = arguments?.getString(getString(R.string.coming_from))


        if (!artistName.isNullOrEmpty() && !albumImage.isNullOrEmpty() && !albumName.isNullOrEmpty()) {
            val bitmap = stringToBitMap(albumImage)
            detailAlbumBinding.albumName.text =
                context?.getString(R.string.album_name_detail, albumName)
            detailAlbumBinding.artistName.text =
                context?.getString(R.string.artist_name_detail, artistName)
            detailAlbumBinding.albumImage.setImageBitmap(bitmap)
        }

        if (comingFrom == getString(R.string.top_albums_fragment)) {
            if (tracksList.isNullOrEmpty() && !noTracks) {
                getAlbumTracksFromApi(albumName.toString(), artistName.toString())
            }
            if (noTracks) detailAlbumBinding.albumTracksTitle.text = getString(R.string.no_trucks)

            detailAlbumBinding.topAlbumButton.text = getString(R.string.save_album)
            detailAlbumBinding.topAlbumButton.setOnClickListener {
                saveAlbum(albumName, artistName, albumImage)
                detailAlbumBinding.seeMyAlbums.visibility = View.VISIBLE
                albumAlreadySaved = true
            }
        } else {
            detailAlbumBinding.seeMyAlbums.visibility = View.GONE
            if (tracksList.isNullOrEmpty() && !noTracks) {
                getAlbumTracksFromDB(albumName.toString(), artistName.toString())
            }
            if (noTracks) detailAlbumBinding.albumTracksTitle.text = getString(R.string.no_trucks)
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
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {

                if (detailAlbumVM.getAlbumsByName(
                        album.albumName.toString(),
                        album.artistName.toString()
                    ).isNotEmpty()
                ) {
                    context?.let { it1 ->
                        createErrorPopup(
                            it1,
                            getString(R.string.album_already_added)
                        ).show()

                        detailAlbumBinding.topAlbumButton.visibility = View.GONE
                    }
                } else {
                    detailAlbumVM.addAlbum(album)
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

    }


    private fun deleteAlbum(albumName: String?, artistName: String?, albumImage: String?) {
        val album = SavedAlbum(albumName, artistName, albumImage)
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                detailAlbumVM.deleteAlbum(album.albumName.toString())
                Toast.makeText(activity, getString(R.string.album_deleted), Toast.LENGTH_SHORT)
                    .show()
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }


    private fun saveAlbumTracks(albumName: String?, artistName: String?) {
        if (tracksList.isNotEmpty()) {
            tracksList.forEach { track ->
                val savedTrack = SavedTrack(track.name, albumName, artistName)
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        detailAlbumVM.addTrack(savedTrack)
                    }
                }
            }

        }


    }


    private fun deleteAlbumTracks(albumName: String?, artistName: String?) {


        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                val listTracksFromDB =
                    detailAlbumVM.getAlbumTracks(albumName.toString(), artistName.toString())
                listTracksFromDB.forEach { savedTrack ->

                    detailAlbumVM.deleteAlbumTracks(
                        savedTrack.trackAlbum.toString(),
                        savedTrack.trackArtist.toString()
                    )

                }
            }


        }
    }


    private fun getAlbumTracksFromDB(albumName: String, artistName: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                tracksList.clear()

                val listTracksFromDB = detailAlbumVM.getAlbumTracks(albumName, artistName)


                listTracksFromDB.forEach { savedTrack ->

                    val track = Track(savedTrack.trackName)
                    tracksList.add(track)
                }

                if (tracksList.isNullOrEmpty()) {
                    detailAlbumBinding.albumTracksTitle.text = getString(R.string.no_trucks)
                    noTracks = true
                } else {
                    trackAdapter =
                        TrackAdapter()
                    tracksRecyclerView.adapter = trackAdapter
                    trackAdapter.submitList(tracksList)
                }


            }
        }

    }


    private fun getAlbumTracksFromApi(album: String, artist: String) {
        lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.Main) {
                detailAlbumVM.getAlbumInfo(album, artist, context!!)
                val loading = createLoadingPopup(context!!, getString(R.string.loading_popup_text))
                detailAlbumVM.flow.collect { it ->
                    when (it) {
                        is ApiState.Loading -> {
                            loading.show()
                        }
                        is ApiState.Failure -> {
                            loading.dismiss()
                            detailAlbumBinding.albumTracksTitle.text = getString(R.string.no_trucks)
                            noTracks = true
                        }
                        is ApiState.SuccessGettingAlbumInfo -> {
                            loading.dismiss()
                            val tracksListResponse = it.data?.trackList
                            if (!tracksListResponse.isNullOrEmpty()) {
                                tracksList = tracksListResponse as MutableList<Track>
                            } else {
                                detailAlbumBinding.albumTracksTitle.text =
                                    getString(R.string.no_trucks)
                                noTracks = true
                            }
                            trackAdapter.submitList(tracksList)


                        }
                        is ApiState.Empty -> {
                            detailAlbumBinding.albumTracksTitle.text = getString(R.string.no_trucks)
                            noTracks = true
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
        trackAdapter.submitList(tracksList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val savedTracksList = savedInstanceState?.getSerializable(TRACKS_LIST_STATE)
            if (savedTracksList != null) {
                tracksList = savedTracksList as MutableList<Track>
            }
            noTracks = savedInstanceState?.getBoolean(NO_TRACKS)
            albumAlreadySaved = savedInstanceState?.getBoolean(ALBUM_ALREADY_SAVED)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (noTracks) {
            outState.putBoolean(NO_TRACKS, true)
        }
        if (albumAlreadySaved) {
            outState.putBoolean(ALBUM_ALREADY_SAVED, true)
        }
        if (!tracksList.isNullOrEmpty()) {
            outState.putSerializable(TRACKS_LIST_STATE, tracksList as ArrayList<out Parcelable?>?)
        }
    }

}