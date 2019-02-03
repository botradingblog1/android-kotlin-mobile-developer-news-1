package com.justmobiledev.mobile_developer_news.news_source_list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.justmobiledev.mobile_developer_news.R
import com.justmobiledev.mobile_developer_news.constants.Constants
import com.justmobiledev.mobile_developer_news.main_menu.MainMenuListActivity
import com.justmobiledev.mobile_developer_news.models.NewsSourceItem
import com.justmobiledev.mobile_developer_news.news_source_detail.NewsSourceDetailActivity
import com.justmobiledev.mobile_developer_news.news_source_detail.NewsSourceDetailFragment
//import kotlinx.android.synthetic.main.fragment_news_source_list.*
import kotlinx.android.synthetic.main.news_source_list_content.view.*

/**
 * A fragment representing a single NewsSourceItem detail screen.
 * This fragment is either contained in a [NewsSourceListActivity]
 * in two-pane mode (on tablets) or a [NewsSourceDetailActivity]
 * on handsets.
 */
class NewsSourceListDetailFragment : Fragment() {
    private val TAG = "NewsSourceListFragment"

    /**
     * The dummy content this fragment is presenting.
     */
    private var newsSourceType = 0
    private var newsSourceTitle = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(Constants.PAGE_ITEM_ID)) {
                newsSourceType = it.getInt(Constants.PAGE_ITEM_ID)
                newsSourceTitle = it.getString(Constants.PAGE_ITEM_NAME)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_news_source_list, container, false)

        val rcView = rootView.findViewById<RecyclerView>(R.id.news_source_list)

        setupRecyclerView(rcView)

        return rootView
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

        recyclerView.adapter = SimpleItemRecyclerViewAdapter(activity as MainMenuListActivity, newsSourceItemList)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: MainMenuListActivity,
        private val values: List<NewsSourceItem>
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as NewsSourceItem
                //if (twoPane) {
                    val fragment = NewsSourceDetailFragment()
                        .apply {
                            arguments = Bundle().apply {
                                val args = Bundle()
                                args.putParcelable(NewsSourceItem.NEWS_SOURCE_ITEM_ID, item)
                            }
                        }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.newssource_detail_container, fragment)
                        .commit()
                //}
                //
                /* else {
                    val intent = Intent(v.context, NewsSourceDetailActivity::class.java).apply {
                        putExtra(NewsSourceItem.NEWS_SOURCE_ITEM_ID, item)
                    }
                    v.context.startActivity(intent)
                }*/
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.news_source_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.id.toString()
            holder.titleView.text = item.title
            holder.descriptionView.text = item.description
            holder.frequencyView.text = item.frequency

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val titleView: TextView = view.title
            val descriptionView : TextView = view.description
            val frequencyView : TextView = view.frequency
            //val contentView: TextView = view.content
        }
    }
}
