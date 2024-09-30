package es.rudo.chatia.presentation.components.suggestionchip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.rudo.chatia.domain.entity.ConfigChat
import es.rudo.chatia.domain.usecase.chat.ChatUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val config: ConfigChat? = null,
    val isLoaded: Boolean = false,
)

class ButtonAIViewModel(private val chatUseCase: ChatUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun getConfig(customerId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val config = chatUseCase.getConfig(customerId)
                _uiState.update {
                    it.copy(config = config)
                }
            } catch (e: Exception) {

            } finally {
                _uiState.update {
                    it.copy(isLoaded = true)
                }
            }
        }
    }
}