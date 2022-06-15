package com.io.appsfactorytesttask.data.network

import android.content.Context
import com.io.appsfactorytesttask.R
import com.io.appsfactorytesttask.data.entities.AlbumTracksResponse
import com.io.appsfactorytesttask.data.entities.SearchArtistsResponse
import com.io.appsfactorytesttask.data.entities.TopAlbumsResponse
import javax.inject.Inject

class LastFmApiImplementation @Inject constructor(private val lastFmAPI: LastFmAPI) {

    suspend fun getTopAlbums(album: String, context: Context): TopAlbumsResponse =
        lastFmAPI.getTopAlbums(album,context.getString(R.string.API_KEY))

    suspend fun searchArtists(artist: String, context: Context): SearchArtistsResponse =
        lastFmAPI.searchArtits(artist,context.getString(
        R.string.API_KEY))

    suspend fun getAlbumInfo(album: String,artist: String, context: Context): AlbumTracksResponse =
        lastFmAPI.getAlbumsInfo(album,artist,context.getString(
            R.string.API_KEY))
}