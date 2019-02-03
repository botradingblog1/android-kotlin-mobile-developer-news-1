package com.justmobiledev.mobile_developer_news.news_source_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.justmobiledev.mobile_developer_news.R
import com.justmobiledev.mobile_developer_news.models.NewsSourceItem
import com.justmobiledev.mobile_developer_news.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_news_source_detail.*

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
    private var newsSourceItem : NewsSourceItem? = null
    private lateinit var adapter: NewsArticleAdapter
    private lateinit var viewModel: NewsSourceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            newsSourceItem = arguments!!.getParcelable(NewsSourceItem.NEWS_SOURCE_ITEM_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_news_source_detail, container, false)

        val recycler_view = rootView.findViewById<RecyclerView>(R.id.recycler_view)
        recycler_view.layoutManager = LinearLayoutManager(this.context)
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.setHasFixedSize(true)

        val progressBar = rootView.findViewById<ProgressBar>(R.id.progressBar)

        // Load News Articles
        viewModel = ViewModelProviders.of(this@NewsSourceDetailFragment).get(NewsSourceViewModel::class.java)
        viewModel.getArticleList().observe(this, Observer { articles ->

            if (articles != null) {
                adapter = NewsArticleAdapter(articles)
                recycler_view.adapter = adapter
                adapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
                swipe_layout.isRefreshing = false
            }

        })

        viewModel.snackbar.observe(this, Observer { value ->
            value?.let {
                Snackbar.make(root_layout, value, Snackbar.LENGTH_LONG).show()
                viewModel.onSnackbarShowed ()
            }

        })

        val swipe_layout = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe_layout)
        swipe_layout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark)
        swipe_layout.canChildScrollUp()
        swipe_layout.setOnRefreshListener {
            adapter.articles.clear()
            adapter.notifyDataSetChanged()
            swipe_layout.isRefreshing = true
            // Fetch news articles
            viewModel.fetchFeed(newsSourceItem!!.url)
        }

        // If no network -> display alert
        val ctx = context ?: return null
        if (!NetworkUtils.isNetworkAvailable(ctx)) {

            val builder = AlertDialog.Builder(ctx)
            builder.setMessage(R.string.network_error_message)
                .setTitle(R.string.network_error_title)
                .setCancelable(false)
                .setPositiveButton(R.string.button_ok)
                {dialog, which ->
                    //
                }


            val alert = builder.create()
            alert.show()

        } else if (NetworkUtils.isNetworkAvailable(ctx)) {
            // Fetch the news articles
            viewModel.fetchFeed(newsSourceItem!!.url)
        }

        return rootView
    }
}
