package com.paulrybitskyi.gamedge.feature_news.domain.datastores

import ca.on.hojat.gamenews.shared.domain.common.entities.Pagination
import com.paulrybitskyi.gamedge.feature_news.domain.entities.Article
import kotlinx.coroutines.flow.Flow

internal interface ArticlesLocalDataStore {
    suspend fun saveArticles(articles: List<Article>)
    fun observeArticles(pagination: Pagination): Flow<List<Article>>
}
