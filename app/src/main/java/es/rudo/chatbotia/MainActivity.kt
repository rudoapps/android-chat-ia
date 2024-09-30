package es.rudo.chatbotia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import es.rudo.chatbotia.ui.navigation.NavigationSetup
import es.rudo.chatbotia.ui.navigation.Screen
import es.rudo.chatbotia.ui.theme.ChatBotIATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatBotIATheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        val navController = rememberNavController()
                        NavigationSetup(
                            navHostController = navController,
                            startDestination = Screen.ChooseChatBotScreen
                        )
                    }
                }
            }
        }
    }
}