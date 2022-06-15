package com.io.appsfactorytesttask.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.io.appsfactorytesttask.data.entities.SavedTrack

@Dao
interface TracksDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlbumTrack(savedTrack: SavedTrack)

    @Query("select * from track where trackAlbum =:albumName and trackArtist = :artistName ")
    suspend fun getAlbumTracks(albumName: String,artistName:String): List<SavedTrack>

    @Query("delete from track where trackAlbum = :albumName and trackArtist = :artistName ")
    suspend fun deleteAlbumTrack(albumName: String,artistName:String)

}