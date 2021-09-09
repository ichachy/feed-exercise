package com.lightricks.feedexercise.data

/**
 * This data class is our internal representation of a feed item.
 */
data class FeedItem(
    val id: String,
    val thumbnailUrl: String,
    val isPremium: Boolean
)
