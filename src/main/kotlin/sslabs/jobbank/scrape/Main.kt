package sslabs.jobbank.scrape

import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.DocElement

fun getDescriptionByUrl(urlToScrape: String): String {
    return skrape(HttpFetcher) {
        request { url = urlToScrape }
        response {
            htmlDocument {
                findFirst(".description-section > div > p:nth-of-type(1)")
            }
        }
    }.toString()
}

fun getWagesByUrl(urlToScrape: String): List<ProvincialWages> {
    return skrape(HttpFetcher) {
        request { url = urlToScrape }
        response {
            htmlDocument {
                findFirst("table#wage-occ-report-nat") {
                    findAll("tbody > tr") {
                        val trs = this
                        extractWages(trs)
                    }
                }
            }
        }
    }
}

fun getProspectsByUrl(urlToScrape: String): JobLabourMarket {
    return skrape(HttpFetcher) {
        request { url = urlToScrape }
        response {
            htmlDocument {
                val provincialProspects = findFirst("table#provoutlooktable_region") {
                    findAll("tbody > tr") {
                        val trs = this
                        extractProvincialProspects(trs)
                    }
                }
                val stats = findAll(".job-market > div > div > div:nth-of-type(2) > p") {
                    this.map{it.text}
                }
                JobLabourMarket(stats[0], stats[1], provincialProspects)
            }
        }
    }
}

private fun extractWages(trs: List<DocElement>): List<ProvincialWages> {
    return trs.map { row ->
        val province = row.findFirst("th").text
        val low = row.findByIndex(0, "td") { text }
        val median = row.findByIndex(1, "td") { text }
        val high = row.findByIndex(2, "td") { text }

        ProvincialWages(province, low, median, high)
    }
}

private fun extractProvincialProspects(trs: List<DocElement>): List<ProvincialProspects> {
    return trs.map { row ->
        val province = row.findFirst("th").text
        val outlook = row.findFirst("td").text
        ProvincialProspects(province,outlook)
    }
}

data class ProvincialWages(
    val province: String,
    val low: String,
    val median: String,
    val high: String,
)

data class ProvincialProspects(
    val province: String,
    val outlook: String,
    )

data class JobLabourMarket (
    val employment2021: String,
    val medianAge2021: String,
    val provincialProspects: List<ProvincialProspects>,
    )


fun main() {
    val occupation = "https://www.jobbank.gc.ca/marketreport/summary-occupation/17887/ca"
    val description = getDescriptionByUrl(occupation)

    val wages = "https://www.jobbank.gc.ca/marketreport/wages-occupation/17887/ca"
    val wagesList = getWagesByUrl(wages)

    val prospects = "https://www.jobbank.gc.ca/marketreport/outlook-occupation/17887/ca"
    val prospectsList = getProspectsByUrl(prospects)
}