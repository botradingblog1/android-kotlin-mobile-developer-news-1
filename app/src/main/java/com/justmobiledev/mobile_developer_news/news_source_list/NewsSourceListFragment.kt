package com.justmobiledev.mobile_developer_news.main_menu

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.justmobiledev.mobile_developer_news.models.NewsMenuItem
import com.justmobiledev.mobile_developer_news.portfolio.PortfolioContent
import kotlinx.android.synthetic.main.activity_news_source_list.*

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [NewsSourceListActivity]
 * in two-pane mode (on tablets) or a [NewsReaderActivity]
 * on handsets.
 */
class NewsSourceListFragment : Fragment() {


    private var item: NewsMenuItem.DummyItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = NewsMenuItem.ITEM_MAP[it.getString(ARG_ITEM_ID)]
            }
            if (it.containsKey(ARG_ITEM_NAME)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                var title = it.getString(ARG_ITEM_NAME)

                activity?.toolbar_layout?.title = title
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var rootView = inflater.inflate(R.layout.fragment_news_source_list, container, false)


        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
        const val ARG_ITEM_NAME = "item_name"
    }
}
