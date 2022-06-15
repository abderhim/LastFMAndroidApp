package com.io.appsfactorytesttask.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "album")
class SavedAlbum (
    @ColumnInfo(name = "albumName")
    var albumName: String?,
    @ColumnInfo(name = "artistName")
    var artistName: String?,
    @ColumnInfo(name = "albumImage")
    var albumImage: String?,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

)