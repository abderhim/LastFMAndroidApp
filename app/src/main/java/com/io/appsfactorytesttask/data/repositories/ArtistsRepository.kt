package com.io.appsfactorytesttask.data.repositories

import android.content.Context
import com.io.appsfactorytesttask.data.entities.SearchArtistsResponse
import com.io.appsfactorytesttask.data.network.LastFmApiImplementation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ArtistsRepository @Inject constructor(private val lastFmApiImplementation: LastFmApiImplementation) {


    fun searchArtists(artist: String, context: Context): Flow<SearchArtistsResponse> = flow {
        emit(lastFmApiImplementation.searchArtists(artist, context))
    }.flowOn(Dispatchers.IO)


}


