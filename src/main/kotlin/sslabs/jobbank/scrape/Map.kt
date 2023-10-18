package sslabs.jobbank.scrape

fun main() {
    val array = listOf(1, 2, 3, 4)
    val squares = array.map(::square)
    val x2 = array.map { i -> i + i }
}

private fun square(num: Int): Int {
    return num * num
}
