package ca.on.hojat.gamenews.feature_news.presentation.mapping

import ca.on.hojat.gamenews.core.formatters.ArticlePublicationDateFormatter
import ca.on.hojat.gamenews.feature_news.domain.entities.Article
import ca.on.hojat.gamenews.feature_news.domain.entities.ImageType
import ca.on.hojat.gamenews.feature_news.presentation.widgets.GamingNewsItemUiModel
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

internal interface GamingNewsItemUiModelMapper {
    fun mapToUiModel(article: Article): GamingNewsItemUiModel
}

@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class GamingNewsItemUiModelMapperImpl @Inject constructor(
    private val publicationDateFormatter: ArticlePublicationDateFormatter
) : GamingNewsItemUiModelMapper {

    override fun mapToUiModel(article: Article): GamingNewsItemUiModel {
        return GamingNewsItemUiModel(
            id = article.id,
            imageUrl = article.imageUrls[ImageType.ORIGINAL],
            title = article.title,
            lede = article.lede,
            publicationDate = article.formatPublicationDate(),
            siteDetailUrl = article.siteDetailUrl
        )
    }

    private fun Article.formatPublicationDate(): String {
        return publicationDateFormatter.formatPublicationDate(publicationDate)
    }
}

internal fun GamingNewsItemUiModelMapper.mapToUiModels(
    articles: List<Article>,
): List<GamingNewsItemUiModel> {
    return articles.map(::mapToUiModel)
}
