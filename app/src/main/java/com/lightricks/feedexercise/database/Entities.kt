package com.lightricks.feedexercise.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "feed_table")
data class FeedEntity(

        @PrimaryKey
        @ColumnInfo(name = "id")val id: String,

        @ColumnInfo(name = "thumbnailUrl") val thumbnailUrl: String,

        @ColumnInfo(name = "isPremium") val isPremium: Boolean
)
