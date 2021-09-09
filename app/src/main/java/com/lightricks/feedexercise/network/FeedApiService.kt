package com.lightricks.feedexercise.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/**
 * Holds a singleton FeedApiService object
 */
interface FeedApiService{

    @GET(FEED_URL)
    fun getFeed() : Single<GetFeedResponse>

    companion object{
        private const val BASE_URL = "https://assets.swishvideoapp.com/Android/demo/"
        private const val FEED_URL = "feed.json" // Relative to BASE_URL

        val instance: FeedApiService by lazy {
            val moshi: Moshi = createMoshi()

            val retrofit: Retrofit = createRetrofit(moshi)

            retrofit.create(FeedApiService::class.java)
        }

        private fun createMoshi() = Moshi.Builder()
                .add(KotlinJsonAdapterFactory()).build()

        private fun createRetrofit(moshi: Moshi): Retrofit {
            return Retrofit.Builder()
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .baseUrl(BASE_URL)
                    .build()
        }
    }
}
