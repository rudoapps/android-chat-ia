package es.rudo.chatia.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.rudo.chatia.App
import es.rudo.chatia.domain.entity.ConfigChat
import es.rudo.chatia.domain.entity.Message
import es.rudo.chatia.domain.usecase.chat.ChatError
import es.rudo.chatia.domain.usecase.chat.ChatUseCase
import es.rudo.chatia.presentation.StoreChatConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val messages: List<Message> = emptyList(),
    val hasGeneralError: Boolean = false,
    val hasConnectionError: Boolean = false,
    val isMessageSent: Boolean = false,
    val isMessageLoading: Boolean = false,
    val indexMessagesError: List<Int> = emptyList(),
    val config: ConfigChat? = null,
    val customerId: Int = 0,
    val isConfigLoaded: Boolean = false,
)

class ChatViewModel(private val chatUseCase: ChatUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun sendMessage(message: String) {
        val messages = _uiState.value.messages.toMutableList()
        messages.add(Message(isResponse = false, content = message.trim()))
        send(message, messages)
        _uiState.update {
            it.copy(
                messages = messages,
                isMessageLoading = true,
                hasGeneralError = false,
                hasConnectionError = false,
            )
        }
    }

    private fun send(message: String, messages: MutableList<Message>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (messages.none { it.isResponse }) {
                    chatUseCase.createChat(1)
                }
                messages.add(chatUseCase.sendMessage(message))
                _uiState.update {
                    UiState(
                        messages = messages,
                        indexMessagesError = it.indexMessagesError,
                        config = it.config,
                        isConfigLoaded = true
                    )
                }
            } catch (e: Exception) {
                addIndexOfMessageError(messages.size - 1)
                when {
                    e is ChatError.NoInternet -> {
                        _uiState.update {
                            it.copy(
                                hasConnectionError = true,
                            )
                        }
                    }

                    else -> {
                        _uiState.update {
                            it.copy(
                                hasGeneralError = true,
                            )
                        }
                    }
                }
            } finally {
                _uiState.update {
                    it.copy(
                        isMessageLoading = false,
                    )
                }
            }
        }
    }

    fun resendMessage() {
        viewModelScope.launch {
            val lastMessage = _uiState.value.messages.last()
            val messages = _uiState.value.messages.toMutableList()
            clearIndexMessageError(messages.size - 1)
            _uiState.update {
                it.copy(
                    hasGeneralError = false,
                    hasConnectionError = false,
                    isMessageLoading = true
                )
            }
            send(lastMessage.content, messages)
        }
    }

    private fun clearIndexMessageError(index: Int) {
        _uiState.update {
            val indexMessagesError = it.indexMessagesError.toMutableList()
            indexMessagesError.remove(index)
            it.copy(indexMessagesError = indexMessagesError)
        }
    }

    private fun addIndexOfMessageError(index: Int) {
        _uiState.update {
            val indexMessagesError = it.indexMessagesError.toMutableList()
            indexMessagesError.add(index)
            it.copy(indexMessagesError = indexMessagesError)
        }
    }

    fun getConfig(customerId: Int) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        config = chatUseCase.getConfig(customerId),
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isConfigLoaded = true
                    )
                }
            }
        }
    }

    fun setConfig(customerId: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(customerId = customerId)
            }
            val chatConfig = StoreChatConfig(App.instance).getConfig.first()
            if (chatConfig != null) {
                _uiState.update {
                    it.copy(
                        config = chatConfig,
                        isConfigLoaded = true
                    )
                }
            } else {
                getConfig(customerId)
            }
        }
    }
}
