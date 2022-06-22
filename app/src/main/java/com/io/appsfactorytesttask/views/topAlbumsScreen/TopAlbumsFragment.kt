package com.io.appsfactorytesttask.views.topAlbumsScreen

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.io.appsfactorytesttask.R
import com.io.appsfactorytesttask.data.entities.Album
import com.io.appsfactorytesttask.data.network.ApiState
import com.io.appsfactorytesttask.databinding.FragmentTopAlbumsBinding
import com.io.appsfactorytesttask.utilities.*
import com.io.appsfactorytesttask.views.adapters.TopAlbumsAdapter
import com.io.appsfactorytesttask.viewModels.TopAlbumsVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class TopAlbumsFragment : Fragment() {
    private var binding: FragmentTopAlbumsBinding? = null
    private val topAlbumsBinding get() = binding!!
    private lateinit var topAlbumsAdapter: TopAlbumsAdapter
    private var topAlbumsList = mutableListOf<Album>()
    private lateinit var topAlbumsVM: TopAlbumsVM
    private lateinit var topAlbumsRecyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopAlbumsBinding.inflate(inflater, container, false)
        topAlbumsAdapter = TopAlbumsAdapter(
            requireContext(),
            activity as AppCompatActivity, getString(R.string.top_albums_fragment)
        )
        topAlbumsVM = ViewModelProvider(this)[TopAlbumsVM::class.java]
        initRecyclerView()

        val arguments = arguments
        val artistName = arguments?.getString(getString(R.string.artist_name))
        if (artistName != null && topAlbumsList.isNullOrEmpty()) {
            context?.let { getTopAlbums(artistName, it) }
        }

        return topAlbumsBinding.root

    }



    private fun initRecyclerView() {
        topAlbumsRecyclerView = topAlbumsBinding.topAlbumsRecyclerView
        val layoutManager = GridLayoutManager(context, 3)
        topAlbumsRecyclerView.layoutManager = layoutManager
        topAlbumsRecyclerView.itemAnimator = DefaultItemAnimator()
        topAlbumsRecyclerView.adapter = topAlbumsAdapter
        topAlbumsAdapter.submitList(topAlbumsList)
    }




    private fun getTopAlbums(artist: String, context: Context) {

        lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.Main) {
                topAlbumsVM.getTopAlbums(artist, context)
                val loading = createLoadingPopup(context, getString(R.string.loading_popup_text))
                topAlbumsVM.flow.collect { it ->
                    when (it) {
                        is ApiState.Loading -> {
                            loading.show()
                        }
                        is ApiState.Failure -> {
                            loading.dismiss()
                            topAlbumsBinding.noTopAlbumsMessage.visibility = View.VISIBLE
                            topAlbumsBinding.topAlbumsRecyclerView.visibility = View.GONE
                            topAlbumsBinding.noTopAlbumsMessage.text =
                                getString(R.string.failure_response_message)

                        }
                        is ApiState.SuccessGettingTopAlbums -> {
                            loading.dismiss()
                            topAlbumsBinding.noTopAlbumsMessage.visibility = View.GONE
                            topAlbumsBinding.topAlbumsRecyclerView.visibility = View.VISIBLE
                            topAlbumsList = it.data.albumsList as MutableList<Album>
                            topAlbumsAdapter.submitList(topAlbumsList)
                            if(topAlbumsList.isEmpty()){
                                topAlbumsBinding.noTopAlbumsMessage.visibility = View.VISIBLE
                                topAlbumsBinding.topAlbumsRecyclerView.visibility = View.GONE
                            }

                        }
                        is ApiState.Empty -> {
                            topAlbumsBinding.noTopAlbumsMessage.visibility = View.VISIBLE
                            topAlbumsBinding.topAlbumsRecyclerView.visibility = View.GONE
                            loading.dismiss()
                        }
                    }
                }
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        if(savedInstanceState!=null ){
            val savedAlbumsList =  savedInstanceState?.getSerializable(ALBUM_LIST_STATE)
            if(savedAlbumsList!=null){ topAlbumsList = savedAlbumsList as MutableList<Album> }
        }
        super.onCreate(savedInstanceState)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if(!topAlbumsList.isNullOrEmpty()){
            outState.putSerializable(ALBUM_LIST_STATE, topAlbumsList as ArrayList<out Parcelable?>?)
        }
    }


}