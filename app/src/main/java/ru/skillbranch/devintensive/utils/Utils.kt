package ru.skillbranch.devintensive.utils

import java.lang.StringBuilder

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.trim()?.split(" ")

        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)

        if (firstName.isNullOrBlank()) {
            firstName = null
        }

        if (lastName.isNullOrBlank()) {
            lastName = null
        }

        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        val sb = StringBuilder()
        var transChar: String

        for (char in payload.trim()) {
            transChar = when (char.toLowerCase()) {
                'а' -> "a"
                'б' -> "b"
                'в' -> "v"
                'г' -> "g"
                'д' -> "d"
                'е' -> "e"
                'ё' -> "e"
                'ж' -> "zh"
                'з' -> "z"
                'и' -> "i"
                'й' -> "i"
                'к' -> "k"
                'л' -> "l"
                'м' -> "m"
                'н' -> "n"
                'о' -> "o"
                'п' -> "p"
                'р' -> "r"
                'с' -> "s"
                'т' -> "t"
                'у' -> "u"
                'ф' -> "f"
                'х' -> "h"
                'ц' -> "c"
                'ч' -> "ch"
                'ш' -> "sh"
                'щ' -> "sh'"
                'ъ' -> ""
                'ы' -> "i"
                'ь' -> ""
                'э' -> "e"
                'ю' -> "yu"
                'я' -> "ya"
                ' ' -> divider
                else -> char.toString()
            }

            if (char.isUpperCase() && transChar.isNotEmpty())
              transChar = transChar[0].toUpperCase() + transChar.substring(1)

            sb.append(transChar)
        }

        return sb.toString()
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        if (firstName.isNullOrBlank() && lastName.isNullOrBlank())
            return null

        return "${firstName?.trim()?.get(0)?.toUpperCase() ?: ""}${lastName?.trim()?.get(0)?.toUpperCase() ?: ""}"
    }
}