package com.justmobiledev.mobile_developer_news.news_source_list

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.justmobiledev.mobile_developer_news.R
import com.prof.rssparser.Article
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_article_row.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class NewsArticleAdapter(val articles: MutableList<Article>) : RecyclerView.Adapter<NewsArticleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.news_article_row, parent, false))

    override fun getItemCount() = articles.size

    override fun onBindViewHolder(holder: NewsArticleAdapter.ViewHolder, position: Int) = holder.bind(articles[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(article: Article) {


            val pubDateString = try {
                val sourceDateString = article.pubDate

                val sourceSdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
                val date = sourceSdf.parse(sourceDateString)

                val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                sdf.format(date)

            } catch (e: ParseException) {
                e.printStackTrace()
                article.pubDate
            }

            itemView.title.text = article.title

            Picasso.get()
                .load(article.image)
                .placeholder(R.drawable.placeholder)
                .into(itemView.image)

            itemView.pubDate.text = pubDateString

            var categories = ""
            for (i in 0 until article.categories.size) {
                categories = if (i == article.categories.size - 1) {
                    categories + article.categories[i]
                } else {
                    categories + article.categories[i] + ", "
                }
            }

            itemView.categories.text = categories

            itemView.setOnClickListener {
                //show article content inside a dialog
                val articleView = WebView(itemView.context)

                articleView.settings.loadWithOverviewMode = true

                articleView.settings.javaScriptEnabled = true
                articleView.isHorizontalScrollBarEnabled = false
                articleView.webChromeClient = WebChromeClient()
                articleView.loadDataWithBaseURL(null, "<style>img{display: inline; height: auto; max-width: 100%;} " +

                        "</style>\n" + "<style>iframe{ height: auto; width: auto;}" + "</style>\n" + article.content, null, "utf-8", null)

                val alertDialog = androidx.appcompat.app.AlertDialog.Builder(itemView.context).create()
                alertDialog.setTitle(article.title)
                alertDialog.setView(articleView)
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK"
                ) { dialog, _ -> dialog.dismiss() }
                alertDialog.show()

                (alertDialog.findViewById<View>(android.R.id.message) as TextView).movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }
}