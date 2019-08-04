package ru.skillbranch.devintensive.models

data class Profile(
    val firstName : String,
    val lastName: String,
    val about: String,
    val repository: String,
    val rating: Int = 0,
    val respect: Int = 0
) {

    val nickName: String = "John Doe" //TODO implement me
    val rank: String = "Junior Android Developer"

    fun toMap(): Map<String, Any> = mapOf(
        "NICK_NAME" to nickName,
        "RANK" to rank,
        "FIRST_NAME" to firstName,
        "LAST_NAME" to lastName,
        "ABOUT" to about,
        "REPOSITORY" to repository,
        "RATING" to rating,
        "RESPECT" to respect
    )
}