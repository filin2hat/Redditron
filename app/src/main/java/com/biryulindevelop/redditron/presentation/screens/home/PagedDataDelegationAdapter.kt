package com.biryulindevelop.redditron.presentation.screens.home

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager

/**Adapter is based on library 'com.hannesdorfmann:adapterdelegates4-kotlin-dsl-viewbinding:4.3.2',
library 'out-of-the-box' is working with outdated Paged List, not Paged Data */

abstract class PagedDataDelegationAdapter<T : Any> : PagingDataAdapter<T, RecyclerView.ViewHolder> {
    protected val delegatesManager: AdapterDelegatesManager<List<T>>

    constructor(
        diffCallback: DiffUtil.ItemCallback<T>,
        vararg delegates: AdapterDelegate<List<T>?>?,
    ) : this(AdapterDelegatesManager<List<T>?>(), diffCallback) {
        for (i in delegates.indices) {
            delegatesManager.addDelegate(delegates[i]!!)
        }
    }

    constructor(diffCallback: DiffUtil.ItemCallback<T>) : this(
        AdapterDelegatesManager<List<T>?>(),
        diffCallback
    )

    constructor(
        delegatesManager: AdapterDelegatesManager<List<T>?>,
        diffCallback: DiffUtil.ItemCallback<T>,
    ) : super(diffCallback) {
        this.delegatesManager = delegatesManager as AdapterDelegatesManager<List<T>>
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)
        delegatesManager.onBindViewHolder(snapshot().items, position, holder, null)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder, position: Int,
        payloads: List<*>,
    ) {
        getItem(position)
        delegatesManager.onBindViewHolder(snapshot().items, position, holder, payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(snapshot().items, position)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return delegatesManager.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewDetachedFromWindow(holder)
    }
}
