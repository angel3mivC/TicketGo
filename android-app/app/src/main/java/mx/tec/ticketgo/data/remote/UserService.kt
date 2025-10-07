package mx.tec.ticketgo.data.remote

import mx.tec.ticketgo.data.models.CreateUserResponse
import mx.tec.ticketgo.data.models.GenericResponse
import mx.tec.ticketgo.data.models.User
import mx.tec.ticketgo.data.models.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @GET("/users/")
    suspend fun getUsers(): Response<List<User>>

    @GET("/users/{id}")
    suspend fun getUsersByID(@Path("id") id: Int): Response<User>

    @POST("/users/")
    suspend fun createUser(@Body user: UserRequest): Response<CreateUserResponse>

    @PUT("/users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: UserRequest): Response<GenericResponse>

    @DELETE("/users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<GenericResponse>
}