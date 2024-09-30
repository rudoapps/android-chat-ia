package es.rudo.chatia.data.datasource.chat.remote.dto

import com.google.gson.annotations.SerializedName

data class CustomerIdDto(
    @SerializedName("customer_id")
    val customerId: Int
)
