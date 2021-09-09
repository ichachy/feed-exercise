package com.lightricks.feedexercise.ui.feed

import androidx.lifecycle.*
import com.lightricks.feedexercise.data.FeedItem
import com.lightricks.feedexercise.data.FeedRepository
import com.lightricks.feedexercise.util.Event
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * This view model manages the data for [FeedFragment].
 */
open class FeedViewModel(private val feedRepository: FeedRepository) : ViewModel() {

    private val stateInternal: MutableLiveData<State> = MutableLiveData<State>(DEFAULT_STATE)
    private val networkErrorEvent = MutableLiveData<Event<String>>()
    private var refreshDisposable: Disposable? = null
    private var feedItemsDisposable: Disposable? = null

    init {
        observeFeedRepository()
        refresh()
    }

    private fun observeFeedRepository() {
        feedItemsDisposable = feedRepository.getAll().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { updatedFeedItems ->
                updateState { copy(feedItems = updatedFeedItems) }
            }
    }

    private fun updateState(transform: State.() -> State) {
        stateInternal.value = transform(getState())
    }

    private fun getState(): State {
        return stateInternal.value!!
    }

    fun refresh() {
        refreshDisposable?.dispose()
        updateState { copy(isLoading = true) }
        refreshDisposable = feedRepository.refresh().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {
                    updateState { copy(isLoading = false) }
                }, {
                    updateState { copy(isLoading = false) }
                    networkErrorEvent.value = Event(NETWORK_ERROR)
                })
    }

    fun getFeedItems(): LiveData<List<FeedItem>> = Transformations.map(stateInternal) {it.feedItems}
    fun getIsEmpty(): LiveData<Boolean> = Transformations.map(stateInternal) {it.feedItems.isNullOrEmpty()}
    fun getIsLoading(): LiveData<Boolean> = Transformations.map(stateInternal) {it.isLoading}

    fun getNetworkErrorEvent(): LiveData<Event<String>> = networkErrorEvent

    override fun onCleared() {
        super.onCleared()
        feedItemsDisposable?.dispose()
        refreshDisposable?.dispose()
    }

    /*
     * Represents the current state of the view model's data
     */
    private data class State(
            val feedItems: List<FeedItem>,
            val isLoading: Boolean)

    companion object{
        private const val NETWORK_ERROR = "Network error has occurred"

        private val DEFAULT_STATE = State(
                feedItems = emptyList(),
                isLoading = false)
    }
}

/**
 * This class creates instances of [FeedViewModel].
 */
class FeedViewModelFactory(private val feedRepository: FeedRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            throw IllegalArgumentException("factory used with a wrong class")
        }
        @Suppress("UNCHECKED_CAST")
        return FeedViewModel(feedRepository) as T
    }
}
