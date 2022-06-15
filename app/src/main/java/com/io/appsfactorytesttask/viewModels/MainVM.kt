package com.io.appsfactorytesttask.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.appsfactorytesttask.data.database.AppDataBase
import com.io.appsfactorytesttask.data.entities.SavedAlbum
import com.io.appsfactorytesttask.data.entities.SavedTrack
import com.io.appsfactorytesttask.data.network.ApiState
import com.io.appsfactorytesttask.data.repositories.ArtistsRepository
import com.io.appsfactorytesttask.data.repositories.TopAlbumsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
 class MainVM@Inject public constructor(
    private val topAlbumsRepository: TopAlbumsRepository,
    private val artistsRepository: ArtistsRepository,
    private val appDataBase: AppDataBase,
    application: Application
) : ViewModel() {


    private val postStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)

    val flow: StateFlow<ApiState> = postStateFlow


    fun getTopAlbums(artist: String, context: Context) = viewModelScope.launch {
        postStateFlow.value = ApiState.Loading
        topAlbumsRepository.getTopAlbums(artist, context)
            .catch { e ->
                postStateFlow.value = ApiState.Failure(e)
            }.collect { data ->
                postStateFlow.value = ApiState.SuccessGettingTopAlbums(data.topAlbums)
            }
    }


    fun getAlbumInfo(album: String, artist: String, context: Context) = viewModelScope.launch {
        postStateFlow.value = ApiState.Loading
        topAlbumsRepository.getAlbumInfo(album, artist, context)
            .catch { e ->
                postStateFlow.value = ApiState.Failure(e)
            }.collect { data ->
                postStateFlow.value = ApiState.SuccessGettingAlbumInfo(data.aLbumTracks?.tracks)
            }
    }


    fun searchArtist(artist: String, context: Context) = viewModelScope.launch {
        postStateFlow.value = ApiState.Loading
        artistsRepository.searchArtists(artist, context)
            .catch { e ->
                postStateFlow.value = ApiState.Failure(e)
            }.collect { data ->
                postStateFlow.value = ApiState.SuccessSearchingArtists(data.results?.artistMatches)
            }
    }


    suspend fun addAlbum(album: SavedAlbum) {
        withContext(Dispatchers.IO) {
            appDataBase.albumsDao().addAlbum(album)
        }
    }

    suspend fun deleteAlbum(albumName: String) {
        withContext(Dispatchers.IO) {
            appDataBase.albumsDao().deleteAlbum(albumName)
        }
    }


    suspend fun getAllAlbums(): List<SavedAlbum> {
        return appDataBase.albumsDao().getAllAlbums()

    }

    suspend fun getAlbumsByName(name: String, artist: String): List<SavedAlbum> {
        return appDataBase.albumsDao().getAlbumsByName(name, artist)

    }


    suspend fun addTrack(savedTrack: SavedTrack) {
        withContext(Dispatchers.IO) {
            appDataBase.tracksDao().addAlbumTrack(savedTrack)
        }
    }

    suspend fun deleteAlbumTracks(albumName: String,artistName: String) {
        withContext(Dispatchers.IO) {
            appDataBase.tracksDao().deleteAlbumTrack(albumName,artistName)
        }
    }

    suspend fun getAlbumTracks(albumName: String, artist: String): List<SavedTrack> {
        return appDataBase.tracksDao().getAlbumTracks(albumName, artist)

    }


}