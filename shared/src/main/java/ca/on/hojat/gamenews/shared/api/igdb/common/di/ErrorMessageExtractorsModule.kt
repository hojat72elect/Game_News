package ca.on.hojat.gamenews.shared.api.igdb.common.di

import ca.on.hojat.gamenews.shared.api.common.ErrorMessageExtractor
import ca.on.hojat.gamenews.shared.api.igdb.common.di.qualifiers.ErrorMessageExtractorKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal interface ErrorMessageExtractorsModule {

    @Binds
    @IntoSet
    fun bindTwitchErrorMessageExtractorToSet(
        @ErrorMessageExtractorKey(ErrorMessageExtractorKey.Type.TWITCH)
        errorMessageExtractor: ErrorMessageExtractor
    ): ErrorMessageExtractor

    @Binds
    @IntoSet
    fun bindIgdbErrorMessageExtractorToSet(
        @ErrorMessageExtractorKey(ErrorMessageExtractorKey.Type.IGDB)
        errorMessageExtractor: ErrorMessageExtractor
    ): ErrorMessageExtractor
}