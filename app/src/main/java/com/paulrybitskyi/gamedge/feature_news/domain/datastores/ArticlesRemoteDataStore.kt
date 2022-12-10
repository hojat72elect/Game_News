package com.paulrybitskyi.gamedge.feature_news.domain.datastores

import ca.on.hojat.gamenews.shared.domain.common.DomainResult
import ca.on.hojat.gamenews.shared.domain.common.entities.Pagination
import com.paulrybitskyi.gamedge.feature_news.domain.entities.Article

internal interface ArticlesRemoteDataStore {
    suspend fun getArticles(pagination: Pagination): DomainResult<List<Article>>
}