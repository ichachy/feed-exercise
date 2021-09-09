package com.lightricks.feedexercise.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.lightricks.feedexercise.database.FeedDatabase
import com.lightricks.feedexercise.network.DummyFeedApiService
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FeedRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var feedRepository: FeedRepository

    private lateinit var database: FeedDatabase

    private val dummyFeedApiService: DummyFeedApiService = DummyFeedApiService()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setDatabase(){
        database = Room.inMemoryDatabaseBuilder(context, FeedDatabase::class.java)
            .build()
        feedRepository = FeedRepository(dummyFeedApiService, database.feedDao())
    }

    @After
    fun closeDatabase(){
        database.close()
    }

    @Test
    fun refresh_completesWithoutErrors(){
        val testObserver = feedRepository.refresh().test()
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun refresh_callRefresh_shouldSaveDataInDatabase(){
        val testObserver = feedRepository.refresh().andThen(database.feedDao().getFeedEntitiesCount()).test()
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue {
                size -> size == DummyFeedApiService.size
        }
    }

    @Test
    fun refreshAndThenGetAll_shouldReturnDataFromGetAll() {
        val testObserver = feedRepository.refresh().andThen(feedRepository.getAll()).test()
        testObserver.assertValue {
            feedItems ->
            assertThat(feedItems).isNotNull()
            assertThat(feedItems).isNotEmpty()
            feedItems == DummyFeedApiService.expectedFeedItems
        }
    }
}
