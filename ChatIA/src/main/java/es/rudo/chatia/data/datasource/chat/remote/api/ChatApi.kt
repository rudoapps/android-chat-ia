package es.rudo.chatia.data.datasource.chat.remote.api

import es.rudo.chatia.data.datasource.chat.remote.dto.CustomerIdDto
import es.rudo.chatia.data.datasource.chat.remote.dto.MessageDto
import es.rudo.chatia.data.datasource.chat.remote.dto.ResponseConfigChatDto
import es.rudo.chatia.data.datasource.chat.remote.dto.ResponseMessageDto
import es.rudo.chatia.data.datasource.chat.remote.dto.ResponseCreateChatDto
import es.rudo.chatia.data.datasource.chat.remote.dto.ResponseDataMessageDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {
    @POST("chat/create")
    suspend fun createChat(@Body customerId: CustomerIdDto): Response<ResponseCreateChatDto>
    @POST("chat/messages/send")
    suspend fun sendMessage(@Body message: MessageDto): Response<ResponseDataMessageDto>
    @POST("chat/assistant/config")
    suspend fun getConfig(@Body customerId: CustomerIdDto): Response<ResponseConfigChatDto>

}