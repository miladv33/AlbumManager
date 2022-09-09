package com.example.quotes.presentation.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.quotes.data.model.generic.LastFMAttr

/**
 * It provides a baseclass for viewModels which call a service to search for something.
 *
 * @constructor Create empty Searchable view model
 */
abstract class SearchableViewModel : BaseViewModel() {
    /**
     * A true value of this variable will prevent the search method from working until it is changed to a false value.
     */
    protected var searching: MutableState<Boolean> = mutableStateOf(false)

    /**
     * what page we are searching
     */
    protected var page: Int = 0

    /**
     * number of result
     */
    protected var restlts: Int = 0

    /**
     * number of Total page
     */
    private var totalPage = -1

    /**
     * number of Total result
     */
    private var totalResult = -1

    /**
     * manage how to search in viewModels.
     *
     * @param T
     * @param list
     * @param name
     * @param startFromZero
     * @param search
     * @receiver
     */
    fun <T> startSearch(
        list: MutableList<T>,
        name: String,
        startFromZero: Boolean,
        search: (name: String, page: Int) -> Unit
    ) {
        if (startFromZero) {
            list.clear()
            searching.value = false
            page = 1
            restlts = 0
            totalPage = -1
            totalResult = -1
        } else {
            if (searching.value || fetchedMaximumPages() || fetchedMaximumResults())
                return
            page++
        }
        searching.value = true
        search.invoke(name, page)
    }

    /**
     * Set total results
     *
     * @param total
     */
    protected fun setTotalResults(total: String?) {
        if (totalResult == -1)
            totalResult = total?.toInt() ?: 0
    }

    /**
     * Set total results
     *
     * @param lastFMAttr
     */
    protected fun setTotalResults(lastFMAttr: LastFMAttr?) {
        if (totalPage == -1)
            totalPage = lastFMAttr?.totalPages?.toInt() ?: 0
    }

    /**
     * Fetched maximum pages
     *
     * @return
     */
     fun fetchedMaximumPages(): Boolean {
        return page == totalPage
    }

    /**
     * Fetched maximum results
     *
     * @return
     */
    fun fetchedMaximumResults(): Boolean {
        return restlts == totalResult
    }
}
