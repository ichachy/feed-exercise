package com.lightricks.feedexercise.data

import com.lightricks.feedexercise.database.FeedDao
import com.lightricks.feedexercise.database.FeedEntity
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.network.GetFeedResponse
import com.lightricks.feedexercise.network.TemplatesMetadataItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

class FeedRepository(private val feedApiService: FeedApiService,
                     private val feedDao: FeedDao) {

    fun getAll(): Flowable<List<FeedItem>>{
        return feedDao.getAll().map {
            it.toFeedItems()
        }
    }

    private fun List<FeedEntity>.toFeedItems(): List<FeedItem> {
        return map {
            FeedItem(it.id, PREFIX_URL + it.thumbnailUrl, it.isPremium)
        }
    }

    fun refresh() : Completable{
        return feedApiService.getFeed()
            .flatMapCompletable { feedResponse -> updateDatabase(feedResponse) }
    }

    private fun updateDatabase(feedResponse: GetFeedResponse): Completable {
        return feedDao.deleteAllAndInsert(feedResponse.templatesMetadataList.toFeedEntities())
    }

    private fun List<TemplatesMetadataItem>.toFeedEntities() : List<FeedEntity> {
        return map {
            FeedEntity(it.id, it.templateThumbnailURI, it.isPremium)
        }
    }

    companion object{
        private const val PREFIX_URL = "https://assets.swishvideoapp.com/" +
                "Android/demo/catalog/thumbnails/"
    }
}
