package es.rudo.chatbotia.ui.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data class ChatBotButtonScreen(val customerId: Int) : Screen()
    @Serializable
    data object ChooseChatBotScreen : Screen()
}