package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel: ViewModel() {

    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository
    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->

        val archiveChats = chats.filter { it.isArchived }

        if (archiveChats.isNotEmpty()) {
            val lastArchiveChat = archiveChats.getLastArchiveChat()
            val unreadableMessagesCount = countAllUnreadableMessages(archiveChats)

            val chatItem: ChatItem = createArchiveChatItem(lastArchiveChat, unreadableMessagesCount)
            val chatsWithoutArchive = chats.filter { !it.isArchived }
                    .map { it.toChatItem() }
                    .sortedBy { it.id.toInt() }

            val result = mutableListOf<ChatItem>()

            return@map result.apply {
                add(0, chatItem)
                addAll(chatsWithoutArchive)
            }
        }

        return@map chats.filter { !it.isArchived }
                .map { it.toChatItem() }
                .sortedBy { it.id.toInt() }
    }

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val chats = chats.value!!

            result.value = if (queryStr.isEmpty()) chats
            else chats.filter { it.title.contains(queryStr, true) }
        }

        result.addSource(chats) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }

        return result
    }

    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)

        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)

        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String) {
        query.value = text
    }

    private fun createArchiveChatItem(archiveChat: Chat, unreadableMessagesCount: Int): ChatItem {
        return ChatItem(
                id = "-1",
                avatar = null,
                initials = "",
                title = "Архив чатов",
                shortDescription = archiveChat.lastMessageShort().first,
                messageCount = unreadableMessagesCount,
                lastMessageDate = archiveChat.lastMessageDate()?.shortFormat(),
                isOnline = false,
                chatType = ChatType.ARCHIVE,
                author = archiveChat.lastMessageShort().second
        )
    }

    private fun List<Chat>.getLastArchiveChat(): Chat {
        val count = countAllUnreadableMessages(this)

        return if (count == 0) this.last()
        else this.filter { it.lastMessageDate() != null }.maxBy { it.lastMessageDate()!! }!!
    }

    private fun countAllUnreadableMessages(chats: List<Chat>): Int {
        return chats.sumBy { it.unreadableMessageCount() }
    }
}