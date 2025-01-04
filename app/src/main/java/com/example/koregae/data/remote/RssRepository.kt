package com.example.koregae.data.remote

import com.example.koregae.data.remote.model.RssItem
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class RssRepository(private val client: HttpClient) {
    suspend fun fetchRssFeed(url: String): List<RssItem> {
        val response: HttpResponse = client.get(url) {
            contentType(ContentType.Application.Xml)
        }

        return parseRss(response.bodyAsText())
    }

    private fun parseRss(xml: String): List<RssItem> {
        val items = mutableListOf<RssItem>()
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(xml.reader())

        var title = ""
        var link = ""
        var description = ""
        var isItem = false

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "item" -> isItem = true
                        "title" -> if (isItem) title = parser.nextText()
                        "link" -> if (isItem) link = parser.nextText()
                        "description" -> if (isItem) description = parser.nextText()
                    }
                }

                XmlPullParser.END_TAG -> {
                    if (parser.name == "item") {
                        items.add(RssItem(title, link, description))
                        isItem = false
                    }
                }
            }
            eventType = parser.next()
        }

        return items
    }
}
