package ir.badesaba.taskmanaer.utils

sealed class Resource<out R> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error<out T>(val errorType: T) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}