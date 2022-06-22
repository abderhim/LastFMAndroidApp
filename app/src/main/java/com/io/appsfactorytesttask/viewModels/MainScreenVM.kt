package com.io.appsfactorytesttask.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.io.appsfactorytesttask.data.database.AppDataBase
import com.io.appsfactorytesttask.data.entities.SavedAlbum
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
 class MainScreenVM@Inject public constructor(
    private val appDataBase: AppDataBase,
    application: Application
) : ViewModel() {

    suspend fun getAllAlbums(): List<SavedAlbum> {
        return appDataBase.albumsDao().getAllAlbums()

    }




}