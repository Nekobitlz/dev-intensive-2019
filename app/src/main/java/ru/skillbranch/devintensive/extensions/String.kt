package ru.skillbranch.devintensive.extensions

fun String.truncate(number: Int = 16): String {
    if (this.trim().length <= number)
        return this.trim()

    val result = this.removeRange(number, this.length)

    return if (result.last() == ' ')
        result.trim().plus("...")
    else result.plus("...")
}