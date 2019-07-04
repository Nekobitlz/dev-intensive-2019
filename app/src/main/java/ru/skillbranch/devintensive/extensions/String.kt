package ru.skillbranch.devintensive.extensions

fun String.truncate(number: Int = 16): String {
    val trimmedString = this.trim()

    if (trimmedString.length <= number)
        return trimmedString

    val result = trimmedString.removeRange(number, trimmedString.length)

    return if (result.last() == ' ')
        result.trim().plus("...")
    else result.plus("...")
}

fun String.stripHtml(): String {
    var result = ""
    var symbols = 0

    while (symbols < this.length) {
        when (this[symbols]) {
            '<' -> while (this[symbols] != '>' && symbols < this.length) { symbols++ }
            '&' -> while (this[symbols] != ';' && symbols < this.length) { symbols++ }
            '>' -> {/* DON'T INSERT */}
            '\'' -> {/* DON'T INSERT */}
            else -> result += this[symbols]
        }

        symbols++
    }

    return result.replace(Regex(" {2,}"), " ")
}