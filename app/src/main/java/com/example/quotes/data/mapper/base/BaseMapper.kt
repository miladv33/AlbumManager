package com.example.quotes.data.mapper.base


/**
 * All mappers inherit the base mapper class
 *
 * @param T
 * @constructor Create empty Base mapper
 */
interface BaseMapper<T> {
    /**
     * In the case of those mappers who work with model classes
     *
     * @param exception
     * @return
     */
    fun mapFailure(exception: Exception): Result<T> = Result.failure(exception)

    /**
     * In the case of those mappers who work with a list of a model
     *
     * @param exception
     * @return
     */
    fun listMapFailure(exception: Exception): Result<List<T>> = Result.failure(exception)
}