package ca.on.hojat.gamenews.shared.api.igdb.common.errorextractors

import ca.on.hojat.gamenews.shared.api.common.ErrorMessageExtractor
import ca.on.hojat.gamenews.shared.api.igdb.common.di.qualifiers.ErrorMessageExtractorKey
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

private const val ERROR_MESSAGE_NAME = "title"

@BindType(withQualifier = true)
@ErrorMessageExtractorKey(ErrorMessageExtractorKey.Type.IGDB)
internal class IgdbErrorMessageExtractor @Inject constructor(
    private val json: Json
) : ErrorMessageExtractor {

    override fun extract(responseBody: String): String = try {
        val rootElement = json.parseToJsonElement(responseBody)
        val rootArray = rootElement.jsonArray
        val rootObject = rootArray.first().jsonObject
        val errorElement = rootObject.getValue(ERROR_MESSAGE_NAME)
        val errorPrimitive = errorElement.jsonPrimitive
        val errorMessage = errorPrimitive.content

        errorMessage
    } catch (expected: Throwable) {
        throw IllegalStateException(
            "Cannot extract a message from the response body: $responseBody",
            expected
        )
    }
}