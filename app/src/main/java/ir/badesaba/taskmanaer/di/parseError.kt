package ir.badesaba.taskmanaer.di

import retrofit2.Response


fun parseError(response: Response<*>): ApiError {
    val errorBody = response.errorBody()?.string()
    val statusCode = response.code()

    val message = when (statusCode) {
        400 -> "Bad Request"
        401 -> "Unauthorized"
        403 -> "Forbidden"
        404 -> "Not Found"
        500 -> "Internal Server Error"
        else -> "Something went wrong"
    }

    return ApiError(statusCode, message)
}

data class ApiError(
    val statusCode: Int,
    val message: String
)