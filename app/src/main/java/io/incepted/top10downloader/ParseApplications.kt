package io.incepted.top10downloader

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class ParseApplications {

    private val TAG = ParseApplications::class.java.simpleName

    val applications = ArrayList<FeedEntry>()

    fun parse(xmlData: String): Boolean {
        Log.d(TAG, "parse called wight $xmlData")
        var status = true
        var inEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentRecord = FeedEntry()
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name?.toLowerCase()
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        Log.d(TAG, "parse: Staring tag for " + tagName)
                        if (tagName == "entry") {
                            inEntry = true
                        }
                    }
                    XmlPullParser.TEXT -> textValue = xpp.text
                    XmlPullParser.END_TAG -> {
                        Log.d(TAG, "parse: Ending tag for " + tagName)
                        if (inEntry) {
                            when (tagName) {
                                "entry" -> {
                                    applications.add(currentRecord)
                                    inEntry = false
                                    currentRecord = FeedEntry() // Create a new object
                                }
                                "name" -> currentRecord.name = textValue
                                "artist" -> currentRecord.artist = textValue
                                "releaseDate" -> currentRecord.releaseDate = textValue
                                "image" -> currentRecord.releaseDate = textValue
                            }
                        }
                    }
                }
                eventType = xpp.next()
            }

            for (app in applications) {
                Log.d(TAG, "************************")
                Log.d(TAG, app.toString())
            }

        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }
        return status
    }


}