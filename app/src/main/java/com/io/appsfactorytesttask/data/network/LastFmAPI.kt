package com.io.appsfactorytesttask.data.network
import com.io.appsfactorytesttask.data.entities.AlbumTracksResponse
import com.io.appsfactorytesttask.data.entities.SearchArtistsResponse
import com.io.appsfactorytesttask.data.entities.TopAlbumsResponse
import retrofit2.http.*


interface LastFmAPI {


    @GET("?method=artist.gettopalbums&format=json")
    suspend fun getTopAlbums(
        @Query("artist")  artist:String,
        @Query("api_key")  apiKey:String,
    ): TopAlbumsResponse

    @GET("?method=artist.search&format=json")
    suspend fun searchArtits(
        @Query("artist")  artist:String,
        @Query("api_key")  apiKey:String,
    ): SearchArtistsResponse

    @GET("?method=album.getinfo&format=json")
    suspend fun getAlbumsInfo(
        @Query("album")  album:String,
        @Query("artist")  artist:String,
        @Query("api_key")  apiKey:String,
    ): AlbumTracksResponse
}