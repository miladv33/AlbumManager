package com.example.quotes.data.repository.base

import com.example.quotes.data.mapper.base.BaseMapper
import com.example.quotes.data.model.Model

/**
 * Safe call repository. Safely calling functions in repositories.
 * Safe calls return failure results instead of throwing an exception.
 *
 * @param T
 * @constructor Create empty Safe call repository
 */
abstract class SafeCallRepository<T : Model> {
    protected abstract val mapper: BaseMapper<T>

    /**
     * Safe call
     *
     * @param call
     * @receiver
     * @return
     */
    suspend fun safeCall(call: suspend () -> Result<T>): Result<T> {
        return try {
            call.invoke()
        } catch (e: Exception) {
            mapper.mapFailure(e)
        }
    }

    /**
     * Safe list call
     *
     * @param call
     * @receiver
     * @return
     */
    suspend fun safeListCall(call: suspend () -> Result<List<T>>): Result<List<T>> {
        return try {
            call.invoke()
        } catch (e: Exception) {
            mapper.listMapFailure(e)
        }
    }
}