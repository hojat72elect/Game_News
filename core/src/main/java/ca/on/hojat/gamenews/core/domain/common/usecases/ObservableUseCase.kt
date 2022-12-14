package ca.on.hojat.gamenews.core.domain.common.usecases

import kotlinx.coroutines.flow.Flow

interface ObservableUseCase<In, Out> {
    fun execute(params: In): Flow<Out>
}

fun <Out> ObservableUseCase<Unit, Out>.execute(): Flow<Out> {
    return execute(Unit)
}