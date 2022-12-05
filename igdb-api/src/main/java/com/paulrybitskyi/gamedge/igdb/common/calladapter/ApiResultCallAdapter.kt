package com.paulrybitskyi.gamedge.igdb.common.calladapter

import com.paulrybitskyi.gamedge.igdb.common.ApiResult
import com.paulrybitskyi.gamedge.igdb.common.ErrorMessageExtractor
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ApiResultCallAdapter<R>(
    private val successType: Type,
    private val errorMessageExtractor: ErrorMessageExtractor
) : CallAdapter<R, Call<ApiResult<R>>> {

    override fun adapt(call: Call<R>): Call<ApiResult<R>> {
        return ApiResultCall(
            delegate = call,
            successType = successType,
            errorMessageExtractor = errorMessageExtractor
        )
    }

    override fun responseType(): Type = successType
}
