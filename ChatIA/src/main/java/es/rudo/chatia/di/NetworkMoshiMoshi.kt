package es.rudo.chatia.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.moshimoshi.network.MoshiMoshi
import com.moshimoshi.network.authenticationcard.api.APIAuthenticationImpl
import com.moshimoshi.network.authenticator.AuthenticatorImpl
import com.moshimoshi.network.entities.BodyType
import com.moshimoshi.network.entities.Endpoint
import com.moshimoshi.network.entities.Method
import com.moshimoshi.network.entities.Parameter
import com.moshimoshi.network.interceptor.auth.AuthInterceptor
import com.moshimoshi.network.storage.datastore.TokenDataStoreImpl
import es.rudo.chatia.data.Config

private val Context.dataStore by preferencesDataStore(
    name = "settings"
)

class NetworkMoshiMoshi( context: Context) {
    private val tokenStore = TokenDataStoreImpl(
        uniqueIdentifier = "APP",
        dataStore = context.dataStore
    )

    private val loginEndpoint = Endpoint(
        url = Config.API_URL,
        method = Method.POST,
        bodyType = BodyType.JSON,
        headers = listOf(
            Parameter(key = "Content-Type", value = "application/json")
        )
    )

    private val authenticationCard = APIAuthenticationImpl(
        loginEndpoint = loginEndpoint,
        refreshEndpoint = null,
        packageName = context.packageName,
        className = "",
        context = context
    )

    private val authenticator = AuthenticatorImpl(
        tokenStore = tokenStore,
        headers = listOf(
            Parameter(key = "X-API-ACCESS-TOKEN", value = Config.CLIENT_SECRET),
            Parameter(key = "X-API-SIGNATURE-MATCH", value = Config.CLIENT_ID)),
        card = authenticationCard
    )

    private val authInterceptor = AuthInterceptor(
        authenticator = authenticator
    )

    val instance = MoshiMoshi(
        baseUrl = Config.API_URL,
        interceptors = arrayOf(authInterceptor),
        authenticator = authenticator
    )
}