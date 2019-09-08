package ru.skillbranch.devintensive.models.data

import androidx.annotation.VisibleForTesting
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.ImageMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class Chat(
        val id: String,
        val title: String,
        val members: List<User> = mutableListOf(),
        var messages: MutableList<BaseMessage> = mutableListOf(),
        var isArchived: Boolean = false
) {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageDate(): Date? = messages.lastOrNull()?.date

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageShort(): Pair<String, String> {
        val lastMessage = messages.lastOrNull()
        val author = lastMessage?.from
        val firstName = author?.firstName
        val messageText = when (lastMessage) {
            is TextMessage -> lastMessage.text ?: "Сообщений пока что нет"
            is ImageMessage -> "$firstName - отправил фото"
            else -> "Сообщений пока что нет"
        }

        return messageText.trim() to "$firstName"
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun unreadableMessageCount(): Int {
        return messages.count { !it.isReaded }
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
                    lastMessageShort().first,
                    unreadableMessageCount(),
                    lastMessageDate()?.shortFormat(),
                    user.isOnline,
                    ChatType.SINGLE,
                    lastMessageShort().second
            )
        } else {
            ChatItem (
                    id,
                    null,
                    "",
                    title,
                    lastMessageShort().first,
                    unreadableMessageCount(),
                    lastMessageDate()?.shortFormat(),
                    false,
                    ChatType.GROUP,
                    lastMessageShort().second
            )
        }
    }
}

enum class ChatType {
    SINGLE,
    GROUP,
    ARCHIVE
}