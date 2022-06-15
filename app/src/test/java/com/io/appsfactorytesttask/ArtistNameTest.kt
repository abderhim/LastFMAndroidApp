package com.io.appsfactorytesttask

import com.io.appsfactorytesttask.utilities.verifyArtistName
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ArtistNameTest {

    @Test
    fun testArtistName() {
        assertTrue(verifyArtistName("Eminem"))
        assertTrue(!verifyArtistName(""))
    }


}