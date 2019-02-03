package com.justmobiledev.mobile_developer_news.news_source_list

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NavUtils
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.justmobiledev.mobile_developer_news.R
import com.justmobiledev.mobile_developer_news.constants.Constants
import com.justmobiledev.mobile_developer_news.models.NewsSourceItem

import kotlinx.android.synthetic.main.activity_newssource_list.*
import kotlinx.android.synthetic.main.newssource_list_content.view.*
//import kotlinx.android.synthetic.main.newssource_list.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [NewsSourceDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class NewsSourceListActivity : AppCompatActivity() {
    val TAG = "NewsSourceListActivity"
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private var newsSourceType = 0
    private var newsSourceTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newssource_list)
        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get the News Type
        newsSourceType = intent.getIntExtra(Constants.PAGE_ITEM_ID, 0)
        newsSourceTitle = intent.getStringExtra(Constants.PAGE_ITEM_NAME)

        if (newssource_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(newssource_list)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                NavUtils.navigateUpFromSameTask(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun setupRecyclerView(recyclerView: RecyclerView) {

        var newsSourceJson = ""
        var newsSourceItemList = listOf<NewsSourceItem>()

        try{
            // Load News Source items from JSON
            if (newsSourceType == Constants.ANDROID_NEWS_ID)
            {
                newsSourceJson = resources.openRawResource(R.raw.android_news_sources).bufferedReader().use { it.readText() }
            }
            else if (newsSourceType == Constants.IOS_NEWS_ID)
            {
                newsSourceJson = resources.openRawResource(R.raw.ios_news_sources).bufferedReader().use { it.readText() }
            }

            val gson = Gson()

            newsSourceItemList = gson.fromJson(newsSourceJson)
        }
        catch(e: Exception){
            Log.d(TAG, e.localizedMessage)
        }


        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, newsSourceItemList, twoPane)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: NewsSourceListActivity,
        private val values: List<NewsSourceItem>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as NewsSourceItem
                if (twoPane) {
                    val fragment = NewsSourceDetailFragment().apply {
                        arguments = Bundle().apply {
                            putInt(NewsSourceItem.NEWS_SOURCE_ITEM_ID, item.id)
                            putString(NewsSourceItem.NEWS_SOURCE_ITEM_TITLE, item.title)
                            putString(NewsSourceItem.NEWS_SOURCE_ITEM_URL, item.url)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.newssource_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, NewsSourceDetailActivity::class.java).apply {
                        putExtra(NewsSourceItem.NEWS_SOURCE_ITEM_ID, item.id)
                        putExtra(NewsSourceItem.NEWS_SOURCE_ITEM_TITLE, item.title)
                        putExtra(NewsSourceItem.NEWS_SOURCE_ITEM_URL, item.url)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.newssource_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.id.toString()
            holder.contentView.text = item.title

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }
}
