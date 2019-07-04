package ru.skillbranch.devintensive.extensions

fun String.truncate(number: Int = 16): String {
    if (this.trim().length <= number)
        return this.trim()

    val result = this.removeRange(number, this.length)

    return if (result.last() == ' ')
        result.trim().plus("...")
    else result.plus("...")
}

fun String.stripHtml(): String {
    var result = ""
    var symbols = 0

    while (symbols < this.length) {
        when (this[symbols]) {
            '<' -> while (this[symbols] != '>') { symbols++ }
            '&' -> {/* DON'T INSERT */}
            '>' -> {/* DON'T INSERT */}
            '\'' -> {/* DON'T INSERT */}
            ' ' -> {
                result += this[symbols]
                while (this[symbols] == ' ') { symbols++ }
                result += this[symbols]
            }
            else -> result += this[symbols]
        }

        symbols++
    }

    return result
}