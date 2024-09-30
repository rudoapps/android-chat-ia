package es.rudo.chatia.core

interface ErrorHandler {
    fun handle(error: Exception): Exception
}