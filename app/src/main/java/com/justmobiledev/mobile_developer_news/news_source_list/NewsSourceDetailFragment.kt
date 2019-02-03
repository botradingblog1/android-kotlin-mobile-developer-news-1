package com.justmobiledev.mobile_developer_news.news_source_list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.justmobiledev.mobile_developer_news.R
import com.justmobiledev.mobile_developer_news.models.NewsSourceItem
import kotlinx.android.synthetic.main.newssource_detail.view.*

/**
 * A fragment representing a single NewsSourceItem detail screen.
 * This fragment is either contained in a [NewsSourceListActivity]
 * in two-pane mode (on tablets) or a [NewsSourceDetailActivity]
 * on handsets.
 */
class NewsSourceDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: NewsSourceItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = NewsSourceItem.ItemMap[it.getString(ARG_ITEM_ID)]
            }
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.newssource_detail, container, false)

        // Show the dummy content as text in a TextView.
        /*item?.let {
            rootView.newssource_detail.text = it.details
        }*/

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
