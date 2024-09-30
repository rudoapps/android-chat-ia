package es.rudo.chatia.core.network

import com.moshimoshi.network.entities.NetworkError
import es.rudo.chatia.core.ErrorHandler
import es.rudo.chatia.domain.entity.AppError

class ErrorHandlerImpl() : ErrorHandler {
    override fun handle(error: Exception): Exception {
        return when(error) {
            is NetworkError.NoInternet -> AppError.NoInternet
            is NetworkError.Timeout -> AppError.Timeout
            is NetworkError.EmptyBody -> AppError.ResponseWithEmptyBody
            is NetworkError.Failure -> AppError.NetworkError(code = error.code, responseErrorBody = error.body)
            else -> AppError.UnknownError
        }
    }
}