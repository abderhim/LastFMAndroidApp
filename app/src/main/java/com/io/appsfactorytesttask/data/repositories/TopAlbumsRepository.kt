package com.io.appsfactorytesttask.data.repositories


import android.content.Context
import com.io.appsfactorytesttask.data.entities.AlbumTracksResponse
import com.io.appsfactorytesttask.data.entities.TopAlbumsResponse
import com.io.appsfactorytesttask.data.network.LastFmApiImplementation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TopAlbumsRepository @Inject constructor(private val lastFmApiImplementation: LastFmApiImplementation)  {

    fun getTopAlbums(artist: String, context: Context):Flow<TopAlbumsResponse> = flow {
        emit(lastFmApiImplementation.getTopAlbums(artist,context))
    }.flowOn(Dispatchers.IO)

    fun getAlbumInfo(album:String,artist: String, context: Context):Flow<AlbumTracksResponse> = flow {
        emit(lastFmApiImplementation.getAlbumInfo(album,artist,context))
    }.flowOn(Dispatchers.IO)

}