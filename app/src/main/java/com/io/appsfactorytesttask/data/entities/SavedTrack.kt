package com.io.appsfactorytesttask.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track")
class SavedTrack(
    @ColumnInfo(name = "trackName")
    var trackName: String?,
    @ColumnInfo(name = "trackAlbum")
    var trackAlbum: String?,
    @ColumnInfo(name = "trackArtist")
    var trackArtist: String?,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)