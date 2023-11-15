package sslabs.jobbank.api

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


private val client = OkHttpClient()

data class Occupation(
    val concordanceId: String,
    val jobTitle: String,
    val noc21Code: String,
    val geo: String,

    )

class OccupationService(
    private val geo: String,
    private val searchStrings: List<String>,
) {
    private fun fetchCAOccupations(): List<Occupation> {
        val occupations: MutableList<Occupation> = mutableListOf()
        for (q in searchStrings) {
            val request = Request.Builder()
                .url("https://www.jobbank.gc.ca/core/ta-jobtitle_en/select?q=$q&wt=json&fq=noc_job_title_type_id:1&fl=title,noc_job_title_concordance_id,noc21_code")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val json = Json.parseToJsonElement(response.body()!!.string()).jsonObject
                val docs = json["response"]!!.jsonObject["docs"]!!.jsonArray
                val occupationSubset = docs.map { doc ->
                    Occupation(
                        concordanceId = doc.jsonObject["noc_job_title_concordance_id"].toString(),
                        jobTitle = doc.jsonObject["title"].toString(),
                        noc21Code = doc.jsonObject["noc21_code"].toString(),
                        geo = this.geo

                    )
                }
                occupations.addAll(occupationSubset)
            }

        }
        return occupations
    }
    fun fetchOccupations(): List<Occupation> {
        return if(geo == "ca") {
            fetchCAOccupations()
        } else emptyList()
    }

}

fun main() {
    val searchStrings = listOf("data", "analytics")
    val occuService = OccupationService("ca", searchStrings)
    print(occuService.fetchOccupations())

}