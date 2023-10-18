package sslabs.jobbank.api

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


private val client = OkHttpClient()

fun main() {
    val request = Request.Builder()
        .url("https://www.jobbank.gc.ca/core/ta-jobtitle_en/select?q=analytics&wt=json&rows=25&fq=noc_job_title_type_id:1&fl=title,noc_job_title_concordance_id,noc21_code")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                print(response.body()!!.string())


    }
}