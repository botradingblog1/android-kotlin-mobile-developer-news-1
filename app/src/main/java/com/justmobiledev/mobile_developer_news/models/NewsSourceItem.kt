package com.justmobiledev.mobile_developer_news.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList
import java.util.HashMap


@Parcelize
data class NewsSourceItem(
    val id: Int,
    val title: String,
    val description: String,
    val frequency: String,
    val url: String) : Parcelable
{
    companion object {
        val NEWS_SOURCE_ITEM_ID = "news_source_item_id"
        val NEWS_SOURCE_ITEM_TITLE = "news_source_item_title"
        val NEWS_SOURCE_ITEM_DESCRIPTION = "news_source_item_description"
        val NEWS_SOURCE_ITEM_URL = "news_source_item_url"
    }

    val Items: MutableList<NewsSourceItem> = ArrayList()


    val ItemMap: MutableMap<String, NewsSourceItem> = HashMap()

    private val COUNT = 25

    init {
    }

    /* private fun addItem(item: NewsSourceItem) {
        Items.add(item)
        ItemMap.put(item.id, item)
    }*/

    /*private fun createDummyItem(position: Int): NewsSourceItem {
        return NewsSourceItem(position.toString(), "Item " + position, makeDetails(position))
    }*/

    /* private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }*/

    /**
     * A dummy item representing a piece of content.
     */
    data class NewsSourceItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}
