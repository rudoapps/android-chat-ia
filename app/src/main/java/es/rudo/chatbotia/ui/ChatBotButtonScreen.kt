package es.rudo.chatbotia.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.rudo.chatia.presentation.components.suggestionchip.ButtonAI

@Composable
fun ChatBotButtonScreen(
    customerId: Int,
    navigateBack: () -> Unit,
    navigateToChat: (Int) -> Unit
){
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Icon(
            modifier = Modifier.clickable { navigateBack() }, imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            ButtonAI(customerId = customerId, navigateToChat = navigateToChat)
        }
    }
}