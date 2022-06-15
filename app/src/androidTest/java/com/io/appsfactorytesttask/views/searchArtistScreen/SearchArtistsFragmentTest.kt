package com.io.appsfactorytesttask.views.searchArtistScreen

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.io.appsfactorytesttask.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import de.sixbits.testrobolectrichilt.launchFragmentInHiltContainer
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SearchArtistsFragmentTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun tournamentsContainerIsDisplayed() {
        launchFragmentInHiltContainer<SearchArtistsFragment>()
        onView(withId(R.id.search_button)).check(matches(isDisplayed()))
        onView(withId(R.id.search_edit_text)).check(matches(isDisplayed()))

    }



}