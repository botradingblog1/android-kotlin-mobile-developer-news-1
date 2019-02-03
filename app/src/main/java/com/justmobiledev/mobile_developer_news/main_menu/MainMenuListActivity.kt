package com.justmobiledev.mobile_developer_news.main_menu

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.justmobiledev.mobile_developer_news.constants.Constants
import com.justmobiledev.mobile_developer_news.models.NewsMenuItem
import com.justmobiledev.mobile_developer_news.news_source_list.NewsSourceListActivity

import kotlinx.android.synthetic.main.activity_main_menu_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.main_menu_list.*

// Main Activity
class MainMenuListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (main_menu_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/layout-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(rc_main_menu_list)

        // Load Detail fragment
        // Tablet Layout
        if (twoPane) {
            val fragment = MainMenuDetailFragment().apply {
                /* arguments = Bundle().apply {
                    putString(Constants.NEWS_SOURCE_ID, "0")
                }*/
            }
            this.supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_menu_detail_container, fragment)
                .commit()

        } else {
            // Phone Layout
            //val intent = Intent(this, NewsSourceListActivity::class.java)
            //this.startActivity(intent)
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {

        val menuItems: MutableList<NewsMenuItem> = ArrayList()

        // Create menu items
        menuItems.add(0,
            NewsMenuItem(
                Constants.MENU_ANDROID_NEWS_ID,
                getString(R.string.title_android_news),
                ""
            )
        )
        menuItems.add(0,
            NewsMenuItem(
                Constants.MENU_IOS_NEWS_ID,
                getString(R.string.title_ios_news),
                ""
            )
        )

        // Set Menu items
        recyclerView.adapter =
                SimpleItemRecyclerViewAdapter(
                    this,
                    menuItems,
                    twoPane
                )
    }

    class SimpleItemRecyclerViewAdapter(private val parentActivity: MainMenuListActivity,
                                        private val values: List<NewsMenuItem>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as NewsMenuItem

                // Tablet Layout
                if (twoPane) {
                    val fragment = NewsSourceListFragment().apply {
                        arguments = Bundle().apply {
                            putString(Constants.PAGE_ITEM_ID, item.id)
                            putString(Constants.PAGE_ITEM_NAME, item.title)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_menu_detail_container, fragment)
                        .commit()

                } else {
                    // Phone Layout
                    val intent = Intent(v.context, NewsSourceListActivity::class.java).apply {
                        putExtra(Constants.PAGE_ITEM_ID, item.id)
                        putExtra(Constants.PAGE_ITEM_NAME, item.title)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_content, parent, false)
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
