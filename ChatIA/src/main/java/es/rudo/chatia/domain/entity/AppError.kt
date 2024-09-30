package es.rudo.chatia.domain.entity

import com.google.gson.Gson

sealed class AppError : Exception() {
    data class NetworkError(val code: Int, private val responseErrorBody: String, var body: ErrorBody?= null) : AppError() {
        init {
            body = parseJSON()
        }
        private fun parseJSON(): ErrorBody {
            return Gson().fromJson(responseErrorBody, ErrorBody::class.java)
        }
    }
    data object UnknownError : AppError()
    data object NoInternet : AppError()
    data object Timeout : AppError()
    data object ResponseWithEmptyBody : AppError()
}

data class ErrorBody(
    val error: ErrorInfo,
)

data class ErrorInfo(
    val field: String?,
    val message: String?,
    val input: String?
)