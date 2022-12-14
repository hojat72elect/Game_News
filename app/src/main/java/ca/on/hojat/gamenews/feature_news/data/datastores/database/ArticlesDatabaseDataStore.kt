package ca.on.hojat.gamenews.feature_news.data.datastores.database

import ca.on.hojat.gamenews.core.data.database.articles.ArticlesTable
import ca.on.hojat.gamenews.core.data.database.articles.DbArticle
import ca.on.hojat.gamenews.core.domain.common.DispatcherProvider
import ca.on.hojat.gamenews.core.domain.entities.Pagination
import ca.on.hojat.gamenews.feature_news.domain.datastores.ArticlesLocalDataStore
import ca.on.hojat.gamenews.feature_news.domain.entities.Article
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
internal class ArticlesDatabaseDataStore @Inject constructor(
    private val articlesTable: ArticlesTable,
    private val dispatcherProvider: DispatcherProvider,
    private val dbArticleMapper: DbArticleMapper
) : ArticlesLocalDataStore {

    override suspend fun saveArticles(articles: List<Article>) {
        articlesTable.saveArticles(
            withContext(dispatcherProvider.computation) {
                dbArticleMapper.mapToDatabaseArticles(articles)
            }
        )
    }

    override fun observeArticles(pagination: Pagination): Flow<List<Article>> {
        return articlesTable.observeArticles(
            offset = pagination.offset,
            limit = pagination.limit
        )
            .toDataArticlesFlow()
    }

    private fun Flow<List<DbArticle>>.toDataArticlesFlow(): Flow<List<Article>> {
        return distinctUntilChanged()
            .map(dbArticleMapper::mapToDomainArticles)
            .flowOn(dispatcherProvider.computation)
    }
}
