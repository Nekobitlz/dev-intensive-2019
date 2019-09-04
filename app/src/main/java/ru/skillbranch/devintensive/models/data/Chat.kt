package ru.skillbranch.devintensive.models.data

import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class Chat(
        val id: String,
        val title: String,
        val members: MutableList<User> = mutableListOf(),
        var messages: MutableList<BaseMessage> = mutableListOf(),
        var isArchived: Boolean = false
) {
    private fun lastMessageDate(): Date? {
        //TODO implement me
        return Date()
    }

    private fun lastMessageShort(): String {
        //TODO implement me
        return "Сообщений еще нет"
    }

    private fun unreadableMessageCount(): Int {
        //TODO implement me
        return 0
    }

    private fun isSingle(): Boolean {
        return members.size == 1
    }

    fun toChatItem(): ChatItem {
        return if (isSingle()) {
            val user = members.first()

            ChatItem (
                    id,
                    user.avatar,
                    Utils.toInitials(user.firstName, user.lastName) ?: "??",
                    "${user.firstName ?: ""} ${user.lastName ?: ""}",
                    lastMessageShort(),
                    unreadableMessageCount(),
                    lastMessageDate()?.shortFormat(),
                    user.isOnline
            )
        } else {
            ChatItem (
                    id,
                    null,
                    "",
                    title,
                    lastMessageShort(),
                    unreadableMessageCount(),
                    lastMessageDate()?.shortFormat(),
                    false
            )
        }
    }
}