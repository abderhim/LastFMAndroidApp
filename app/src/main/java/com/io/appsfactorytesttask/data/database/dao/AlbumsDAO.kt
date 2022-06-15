package com.io.appsfactorytesttask.data.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.io.appsfactorytesttask.data.entities.SavedAlbum

@Dao
interface AlbumsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlbum(album: SavedAlbum)

    @Query("select * from album")
    suspend fun getAllAlbums(): List<SavedAlbum>

    @Query("select * from album where albumName = :name and artistName =:artistName")
    suspend fun getAlbumsByName(name: String,artistName:String): List<SavedAlbum>

    @Query("delete from album where albumName = :name ")
    suspend fun deleteAlbum(name:String)

}