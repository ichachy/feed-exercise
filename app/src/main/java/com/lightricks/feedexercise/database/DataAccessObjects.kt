package com.lightricks.feedexercise.database

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
abstract class FeedDao{

    @Query("SELECT * FROM feed_table")
    abstract fun getAll(): Flowable<List<FeedEntity>>

    @Query("SELECT COUNT(id) FROM feed_table")
    abstract fun getFeedEntitiesCount(): Single<Int>

    fun deleteAllAndInsert(feedEntitiesList: List<FeedEntity>): Completable{
        return Completable.fromAction {
            deleteAllAndInsertSync(feedEntitiesList)
        }
    }

    @Transaction
    protected open fun deleteAllAndInsertSync(feedEntitiesList: List<FeedEntity>){
        deleteAllSync()
        insertSync(feedEntitiesList)
    }

    @Query("DELETE FROM feed_table")
    protected abstract fun deleteAllSync()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertSync(feedEntitiesList: List<FeedEntity>)
}
