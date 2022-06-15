package com.io.appsfactorytesttask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.io.appsfactorytesttask.data.database.AppDataBase
import com.io.appsfactorytesttask.data.database.dao.AlbumsDAO
import com.io.appsfactorytesttask.data.entities.SavedAlbum
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import org.junit.*
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest
class DatabaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDataBase
    private lateinit var albumsDAO: AlbumsDAO

    @Before
    fun setup() {
        hiltRule.inject()
        albumsDAO = database.albumsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAlbum() = runBlocking {
        val savedAlbum = SavedAlbum("Recovery", "Eminem", "recovery.png", 10)
        albumsDAO.addAlbum(savedAlbum)
        val albums = albumsDAO.getAllAlbums()
        Assert.assertEquals(1, albums.size);
    }

    @Test
    fun deleteAlbum() = runBlocking {
        albumsDAO.deleteAlbum("Recovery")
        val albums = albumsDAO.getAllAlbums()
        Assert.assertEquals(0, albums.size);
    }

}