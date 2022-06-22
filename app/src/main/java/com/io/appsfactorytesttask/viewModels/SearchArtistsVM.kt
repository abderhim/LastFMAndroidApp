package com.io.appsfactorytesttask.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.appsfactorytesttask.data.network.ApiState
import com.io.appsfactorytesttask.data.repositories.ArtistsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchArtistsVM@Inject public constructor(
    private val artistsRepository: ArtistsRepository,
    application: Application
) : ViewModel() {


    private val postStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)

    val flow: StateFlow<ApiState> = postStateFlow

    fun searchArtist(artist: String, context: Context) = viewModelScope.launch {
        postStateFlow.value = ApiState.Loading
        artistsRepository.searchArtists(artist, context)
            .catch { e ->
                postStateFlow.value = ApiState.Failure(e)
            }.collect { data ->
                postStateFlow.value = ApiState.SuccessSearchingArtists(data.results?.artistMatches)
            }
    }


}