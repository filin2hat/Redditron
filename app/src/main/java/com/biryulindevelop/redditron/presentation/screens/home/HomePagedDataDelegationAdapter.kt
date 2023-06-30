package com.biryulindevelop.redditron.presentation.screens.home

import com.biryulindevelop.redditron.domain.ListItem
import com.biryulindevelop.redditron.domain.tools.ClickableView
import com.biryulindevelop.redditron.domain.tools.SubQuery
import com.biryulindevelop.redditron.presentation.delegates.postsDelegate
import com.biryulindevelop.redditron.presentation.delegates.subredditsDelegate
import com.biryulindevelop.redditron.presentation.utils.ListItemDiffUtil

class HomePagedDataDelegationAdapter(
    private val onClick: (subQuery: SubQuery, item: ListItem, clickableView: ClickableView) -> Unit,
) : PagedDataDelegationAdapter<ListItem>(ListItemDiffUtil()) {
    init {
        delegatesManager.addDelegate(subredditsDelegate { subQuery: SubQuery, listItem: ListItem, clickableView: ClickableView ->
            onClick(subQuery, listItem, clickableView)
        }
        )
            .addDelegate(postsDelegate { subQuery: SubQuery, listItem: ListItem, clickableView: ClickableView ->
                onClick(subQuery, listItem, clickableView)
            }
            )
    }
}