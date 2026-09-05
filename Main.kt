fun main() {
    val punctuationRegex = Regex("""[\p{P}\s]+""")
    println(" ".matches(punctuationRegex))
}
