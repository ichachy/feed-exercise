package com.lightricks.feedexercise.network

import com.lightricks.feedexercise.data.FeedItem
import com.lightricks.feedexercise.database.FeedEntity
import com.lightricks.feedexercise.data.FeedRepository
import io.reactivex.rxjava3.core.Single

/**
 * This class is used to test [FeedRepository]
 */
class DummyFeedApiService: FeedApiService{

    override fun getFeed(): Single<GetFeedResponse> {
        return Single.just(response)
    }

    companion object{
        private val item1: TemplatesMetadataItem = TemplatesMetadataItem("a", "1", true, true,  listOf("a", "1"), "a", "a1")
        private val item2: TemplatesMetadataItem = TemplatesMetadataItem("b", "2", true, false,  listOf("b", "2"), "b", "b2")
        private val item3: TemplatesMetadataItem = TemplatesMetadataItem("c", "3", false, true,  listOf("c", "3"), "c", "c3")
        private val item4: TemplatesMetadataItem = TemplatesMetadataItem("d", "4", false, false,  listOf("d", "4"), "d", "d4")
        private val item5: TemplatesMetadataItem = TemplatesMetadataItem("e", "5", true, true,  listOf("e", "5"), "e", "e5")
        private val item6: TemplatesMetadataItem = TemplatesMetadataItem("f", "6", true, true,  listOf("f", "6"), "f", "f6")

        private val response = GetFeedResponse(listOf(item1, item2, item3, item4, item5, item6))

        val size = response.templatesMetadataList.size

        private const val prefix = "https://assets.swishvideoapp.com/Android/demo/catalog/thumbnails/"

        private val feedItem1: FeedItem = FeedItem("1", prefix + "a1", true)
        private val feedItem2: FeedItem = FeedItem("2", prefix + "b2", false)
        private val feedItem3: FeedItem = FeedItem("3", prefix + "c3", true)
        private val feedItem4: FeedItem = FeedItem("4", prefix + "d4", false)
        private val feedItem5: FeedItem = FeedItem("5", prefix + "e5", true)
        private val feedItem6: FeedItem = FeedItem("6", prefix + "f6", true)

        val expectedFeedItems = listOf(feedItem1, feedItem2, feedItem3, feedItem4, feedItem5, feedItem6)
    }
}
