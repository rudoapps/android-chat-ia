package es.rudo.chatia.presentation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import es.rudo.chatia.domain.entity.ConfigChat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreChatConfig(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("chatConfig")
        val CHAT_CONFIG_COLOR_1_KEY = stringPreferencesKey ("chat_config_color_one")
        val CHAT_CONFIG_COLOR_2_KEY = stringPreferencesKey ("chat_config_color_two")
    }

    val getConfig: Flow<ConfigChat?> = context.dataStore.data
        .map { preferences ->
            val colorOne = preferences[CHAT_CONFIG_COLOR_1_KEY]
            val colorTwo = preferences[CHAT_CONFIG_COLOR_2_KEY]
            if (colorOne != null && colorTwo != null) {
                ConfigChat(colorOne = colorOne, colorTwo = colorTwo, firstMessage = "")
            } else {
                null
            }
        }

    suspend fun saveConfig(colorOne: String, colorTwo: String) {
        context.dataStore.edit { preferences ->
            preferences.clear()
            preferences[CHAT_CONFIG_COLOR_1_KEY] = colorOne
            preferences[CHAT_CONFIG_COLOR_2_KEY] = colorTwo
        }
    }
}