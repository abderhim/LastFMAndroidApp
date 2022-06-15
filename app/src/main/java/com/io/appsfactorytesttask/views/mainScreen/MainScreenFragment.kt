package com.io.appsfactorytesttask.views.mainScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.io.appsfactorytesttask.R
import com.io.appsfactorytesttask.data.entities.Album
import com.io.appsfactorytesttask.data.entities.AlbumImage
import com.io.appsfactorytesttask.data.entities.Artist
import com.io.appsfactorytesttask.databinding.FragmentMainScreenBinding
import com.io.appsfactorytesttask.utilities.showFragment
import com.io.appsfactorytesttask.viewModels.MainVM
import com.io.appsfactorytesttask.views.adapters.TopAlbumsAdapter
import com.io.appsfactorytesttask.views.searchArtistScreen.SearchArtistsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    private var binding: FragmentMainScreenBinding? = null
    private val mainScreenBinding get() = binding!!
    private lateinit var mainVM: MainVM
    private var topAlbumsList = mutableListOf<Album>()
    private lateinit var topAlbumsRecyclerView: RecyclerView
    private lateinit var topAlbumsAdapter: TopAlbumsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainScreenBinding.inflate(inflater, container, false)

        topAlbumsAdapter = TopAlbumsAdapter(
            requireContext(),
            activity as AppCompatActivity, getString(R.string.main_screen_fragment)
        )

        mainVM = ViewModelProvider(this).get(MainVM::class.java)

        initRecyclerView()
        getUserTopAlbums()

        mainScreenBinding.btnShowSearchScreen.setOnClickListener {
            showFragment(activity as AppCompatActivity, SearchArtistsFragment())
        }

        return mainScreenBinding.root
    }


    private fun initRecyclerView() {
        topAlbumsRecyclerView = mainScreenBinding.topAlbumsRecyclerView
        val layoutManager = GridLayoutManager(context, 3)
        topAlbumsRecyclerView.layoutManager = layoutManager
        topAlbumsRecyclerView.itemAnimator = DefaultItemAnimator()
        topAlbumsRecyclerView.adapter = topAlbumsAdapter
        topAlbumsAdapter.differ.submitList(topAlbumsList)
    }


    private fun getUserTopAlbums() {
        GlobalScope.launch(Dispatchers.Main) {
            mainScreenBinding.topAlbumsRecyclerView.visibility = View.VISIBLE
            mainScreenBinding.noAlbumsTextView.visibility = View.GONE
            topAlbumsList.clear()

            mainVM.getAllAlbums().forEach { savedAlbum ->
                val imagesList = ArrayList<AlbumImage>()
                val albumImage = AlbumImage(savedAlbum.albumImage)
                imagesList.add(albumImage)
                val artist = Artist(savedAlbum.artistName, null)
                val album = Album(savedAlbum.albumName, imagesList, artist)
                if (!topAlbumsList.contains(album)) {
                    topAlbumsList.add(album)
                }
            }

            if (topAlbumsList.isNullOrEmpty()) {
                mainScreenBinding.topAlbumsRecyclerView.visibility = View.GONE
                mainScreenBinding.noAlbumsTextView.visibility = View.VISIBLE
            } else {
                mainScreenBinding.myTopAlbums.visibility=View.VISIBLE
                topAlbumsAdapter =
                    TopAlbumsAdapter(
                        requireContext(),
                        activity as AppCompatActivity,
                        getString(R.string.main_screen_fragment)
                    )
                topAlbumsRecyclerView.adapter = topAlbumsAdapter
                topAlbumsAdapter.differ.submitList(topAlbumsList)
            }

        }
    }

}