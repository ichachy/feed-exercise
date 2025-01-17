package com.lightricks.feedexercise.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO class. Maps a JSON into a list of [TemplatesMetadataItem]
 */
@JsonClass(generateAdapter = true)
data class GetFeedResponse(
    @Json(name = "templatesMetadata") val templatesMetadataList: List<TemplatesMetadataItem>)

@JsonClass(generateAdapter = true)
data class TemplatesMetadataItem(
        @Json(name = "configuration") val configuration: String,
        @Json(name = "id") val id: String,
        @Json(name = "isNew") val isNew: Boolean,
        @Json(name = "isPremium") val isPremium: Boolean,
        @Json(name = "templateCategories") val templateCategories: List<String>,
        @Json(name = "templateName") val templateName: String?,
        @Json(name = "templateThumbnailURI") val templateThumbnailURI: String)
