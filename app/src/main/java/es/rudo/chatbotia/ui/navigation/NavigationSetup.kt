package es.rudo.chatbotia.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import es.rudo.chatbotia.ui.ChatBotButtonScreen
import es.rudo.chatbotia.ui.ChooseChatBotScreen
import es.rudo.chatia.presentation.chat.ChatAI
import es.rudo.chatia.presentation.navigation.ChatScreen

@Composable
fun NavigationSetup(
    navHostController: NavHostController,
    startDestination: Screen,
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize(),
    ) {
        val popBackStack: () -> Unit = {
            navHostController.popBackStack()
        }
        val navigateToChatScreen: (Int) -> Unit = { customerId ->
            navHostController.navigate(ChatScreen(customerId))
        }

        val navigateToChatBotScreen: (Int) -> Unit = {
            navHostController.navigate(Screen.ChatBotButtonScreen(it))
        }

        composable<ChatScreen> { backStackEntry ->
            val customerId = backStackEntry.toRoute<ChatScreen>().customerId
            ChatAI(customerId = customerId, navigateBack = popBackStack)
        }

        composable<Screen.ChatBotButtonScreen> {
            val customerId = it.toRoute<Screen.ChatBotButtonScreen>().customerId
            ChatBotButtonScreen(
                customerId = customerId,
                navigateBack = popBackStack,
                navigateToChat = navigateToChatScreen
            )
        }

        composable<Screen.ChooseChatBotScreen> {
            ChooseChatBotScreen(navigateToChatBot = navigateToChatBotScreen)
        }
    }
}