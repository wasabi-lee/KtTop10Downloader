package io.incepted.top10downloader

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import java.net.URL

import android.content.Context
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates


class FeedEntry {
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageURL: String = ""

    override fun toString(): String {
        return """
            name = $name
            artist = $artist
            releaseDate = $releaseDate
            summary = $summary
            imageURL = $imageURL
            """.trimIndent()
    }

}


class MainActivity : AppCompatActivity() {
    companion object { // Kotlin's equivalent of static.
        private val TAG: String = MainActivity::class.java.simpleName

        private class DownloadData(context: Context, listview: ListView) : AsyncTask<String, Void, String>() {

            private val TAG = DownloadData::class.java.simpleName

            var propContext: Context by Delegates.notNull()
            var propListView: ListView by Delegates.notNull()

            init {
                propContext = context
                propListView = listview
            }


            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
//                Log.d(TAG, "onPostExecute: parameter is $result")
                val parseApplications = ParseApplications()
                if (result != null)
                    parseApplications.parse(result)

                val arrayAdaptor = ArrayAdapter<FeedEntry>(propContext, R.layout.list_item, parseApplications.applications)
                propListView.adapter = arrayAdaptor
            }


            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG, "doInBackground: starts with ${url[0]}")
                val rssFeed = downloadXML(url[0])
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "doInBackground: Error downloading")
                }
                return rssFeed
            }

            private fun downloadXML(urlPath: String?): String {
                return URL(urlPath).readText() // Kotlin extension function
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate")

        val downloadData = DownloadData(this, main_listview)
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG, "onCreate: done")

    }


}
