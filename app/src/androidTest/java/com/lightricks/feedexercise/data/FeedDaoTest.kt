package com.lightricks.feedexercise.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.*
import com.lightricks.feedexercise.database.FeedDao
import com.lightricks.feedexercise.database.FeedDatabase
import com.lightricks.feedexercise.database.FeedEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeedDaoTest {

    private lateinit var feedDao: FeedDao

    private lateinit var feedDatabase: FeedDatabase

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setDatabase(){
        feedDatabase = Room.inMemoryDatabaseBuilder(context, FeedDatabase::class.java)
            .build()
        feedDao = feedDatabase.feedDao()
    }


    @After
    fun closeDatabase(){
        feedDatabase.close()
    }

    @Test
    fun getTableSize_initialTableSize_shouldReturn0(){
        val testObserver = feedDao.getFeedEntitiesCount().test()
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue {
            size -> size == 0
        }
        testObserver.dispose()
    }

    @Test
    fun deleteAllAndInsert_shouldChangeDatabaseAccordingly(){
        val testObserver = feedDao.deleteAllAndInsert(list1).andThen(feedDao.getAll()).test()
        testObserver.assertValue{
                list ->
            list == list1
            }
    }

    @Test
    fun deleteAllAndInsert_shouldChangeTableSize(){
        val testObserver = feedDao.deleteAllAndInsert(list1).andThen(feedDao.getFeedEntitiesCount()).test()
        testObserver.assertValue{
                size -> size == list1.size
        }
        testObserver.dispose()
    }

    @Test
    fun multipleDeleteAllAndInsert_shouldChangeDatabaseAccordingly(){
        val testObserver = feedDao.deleteAllAndInsert(list1)
            .andThen(feedDao.deleteAllAndInsert(list2))
            .andThen(feedDao.getAll())
            .test()
        testObserver.assertValue{
                list ->
            assertThat(list).isNotEqualTo(list1)
            for (item in list1){
                assertThat(list).doesNotContain(item)
            }
            assertThat(list).isNotEmpty()
            list == list2
        }
    }

    @Test
    fun deleteAllAndInsert_insertEmptyList_shouldChangeDatabaseAccordingly(){
        val testObserver = feedDao.deleteAllAndInsert(emptyList).andThen(feedDao.getFeedEntitiesCount()).test()
        testObserver.assertValue{
                size -> size == 0
        }
        testObserver.dispose()
    }

    companion object{
        private val feedEntity1: FeedEntity = FeedEntity("1", "1", true)
        private val feedEntity2: FeedEntity = FeedEntity("2", "2", true)
        private val feedEntity3: FeedEntity = FeedEntity("3", "3", true)
        private val feedEntity4: FeedEntity = FeedEntity("4", "4", true)

        private val list1 = listOf(feedEntity1, feedEntity2)
        private val list2 = listOf(feedEntity3, feedEntity4)
        private val emptyList = emptyList<FeedEntity>()
    }
}
