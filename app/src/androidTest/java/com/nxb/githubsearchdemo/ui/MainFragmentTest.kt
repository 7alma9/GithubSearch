package com.nxb.githubsearchdemo.ui

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.nxb.githubsearchdemo.R
import com.nxb.githubsearchdemo.launchFragmentInHiltContainer
import com.nxb.githubsearchdemo.mockTest
import com.nxb.githubsearchdemo.util.RecyclerViewMatcher
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject


@HiltAndroidTest
@ExperimentalCoroutinesApi
@MediumTest
 class MainFragmentTest {
    @Inject
    lateinit var customFragmentFactory: TestCustomFragmentFactory

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    lateinit var  mainViewModel:MainViewModel
    @Before
    fun setup() {
        hiltRule.inject()
         mainViewModel= mockTest()

    }


    @Test
    fun testScrolling() {

        launchFragmentInHiltContainer<MainFragment>(fragmentFactory = customFragmentFactory) {

        }
        onView(withId(R.id.progress)).check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.search_view)).perform(typeSearchViewText("salman"))
         val action = RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(9)

        onView(withId(R.id.rv_repos)).perform(action)

        onView(listMatcher().atPosition(9)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun testIntent(){
        launchFragmentInHiltContainer<MainFragment>(fragmentFactory = customFragmentFactory) {
        }
        onView(withId(R.id.search_view)).perform(typeSearchViewText("salman",true))
        onView(listMatcher().atPosition(0)).perform(click())
        intended(toPackage("android.intent.action.VIEW"))
    }


    @Test
    fun testDataLoad() {

        launchFragmentInHiltContainer<MainFragment>(fragmentFactory = customFragmentFactory) {

        }
        onView(withId(R.id.progress)).check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.search_view)).perform(typeSearchViewText("salman"))
        onView(listMatcher().atPosition(0)).check(ViewAssertions.matches(isDisplayed()))

    }

    @Test
    fun testClickOnRepo() {
         val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<MainFragment>(fragmentFactory = customFragmentFactory) {
            Navigation.setViewNavController(requireView(),navController)
        }
        onView(withId(R.id.progress)).check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.search_view)).perform(typeSearchViewText("salman"))
        onView(listMatcher().atPosition(0)).check(ViewAssertions.matches(isDisplayed()))
        onView(listMatcher().atPosition(0)).perform(click())
            Mockito.verify(navController).navigate(MainFragmentDirections.actionMainFragmentToRepoDetailFragment())
    }


    fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.rv_repos)

    }

    fun typeSearchViewText(text: String,submit:Boolean=true): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return text
            }

            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as SearchView).setQuery(text, submit)
            }
        }
    }


}