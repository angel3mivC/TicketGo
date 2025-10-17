package mx.tec.ticketgo.data.remote

import mx.tec.ticketgo.data.models.CreateUserResponse
import mx.tec.ticketgo.data.models.GenericResponse
import mx.tec.ticketgo.data.models.GetUserResponse
import mx.tec.ticketgo.data.models.CreateUserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @GET("users/")
    suspend fun getUsers(): Response<List<GetUserResponse>>

    @GET("users/technicians")
    suspend fun getTechnicians(): Response<List<GetUserResponse>>

    @GET("users/{id}")
    suspend fun getUserByID(@Path("id") id: Int): Response<GetUserResponse>

    @POST("users/")
    suspend fun createUser(@Body request: CreateUserRequest): Response<CreateUserResponse>

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body request: CreateUserRequest): Response<GenericResponse>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<GenericResponse>
}