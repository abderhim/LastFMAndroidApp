package com.io.appsfactorytesttask.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.appsfactorytesttask.data.network.ApiState
import com.io.appsfactorytesttask.data.repositories.TopAlbumsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TopAlbumsVM@Inject public constructor(
    private val topAlbumsRepository: TopAlbumsRepository,
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


}