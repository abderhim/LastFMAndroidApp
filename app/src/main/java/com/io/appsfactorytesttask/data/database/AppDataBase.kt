package com.io.appsfactorytesttask.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.io.appsfactorytesttask.R
import com.io.appsfactorytesttask.data.database.dao.AlbumsDAO
import com.io.appsfactorytesttask.data.database.dao.TracksDAO
import com.io.appsfactorytesttask.data.entities.SavedAlbum
import com.io.appsfactorytesttask.data.entities.SavedTrack

@Database(entities = [SavedAlbum::class, SavedTrack::class], version = 13)
abstract class AppDataBase : RoomDatabase() {
    abstract fun albumsDao(): AlbumsDAO
    abstract fun tracksDao(): TracksDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            val tempInstance = INSTANCE
            tempInstance?.let { return tempInstance }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    context.getString(R.string.app_database_name)
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}