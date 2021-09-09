package com.lightricks.feedexercise.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Holds a singleton object of the database
 */
@Database(entities = [FeedEntity::class], version = 1)
abstract class FeedDatabase : RoomDatabase(){

    abstract fun feedDao(): FeedDao

    companion object{
        private const val DATABASE_NAME = "feed_database"

        @Volatile
        private var INSTANCE: FeedDatabase? = null

        fun getInstance(context: Context): FeedDatabase{
            var instance :FeedDatabase? = INSTANCE
            if (instance == null){
                synchronized(this){
                    instance = INSTANCE
                    if (instance == null){
                        instance = createDatabase(context)
                        INSTANCE = instance
                    }
                }
            }
            return instance!!
        }

        private fun createDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        FeedDatabase::class.java, DATABASE_NAME)
                        .build()
    }
}
