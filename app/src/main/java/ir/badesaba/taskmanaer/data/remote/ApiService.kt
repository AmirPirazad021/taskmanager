package ir.badesaba.taskmanaer.data.remote

import ir.badesaba.taskmanaer.data.TasksDto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("https://run.mocky.io/v3/0f0b1737-7b2a-49b9-8508-046ff4435688")
    suspend fun getTasks(): List<TasksDto>

}