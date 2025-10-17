package mx.tec.ticketgo.data.repository

import mx.tec.ticketgo.data.models.GetUserResponse
import mx.tec.ticketgo.data.models.CreateUserRequest
import mx.tec.ticketgo.data.models.CreateUserResponse
import mx.tec.ticketgo.data.models.GenericResponse
import mx.tec.ticketgo.data.network.ApiClient
import mx.tec.ticketgo.data.remote.UserService
import mx.tec.ticketgo.data.utils.safeApiCall
class UserRepository() {
    private val service: UserService = ApiClient.retrofit.create(UserService::class.java)

    suspend fun getUsers(): Result<List<GetUserResponse>> =
        safeApiCall { service.getUsers() }

    suspend fun getTechnicians(): Result<List<GetUserResponse>> =
        safeApiCall { service.getTechnicians() }

    suspend fun getUserById(id: Int): Result<GetUserResponse> =
        safeApiCall { service.getUserByID(id) }

    suspend fun createUser(request: CreateUserRequest): Result<CreateUserResponse> =
        safeApiCall { service.createUser(request) }

    suspend fun updateUser(id: Int, request: CreateUserRequest): Result<GenericResponse> =
        safeApiCall { service.updateUser(id, request) }

    suspend fun deleteUser(id: Int): Result<GenericResponse> =
        safeApiCall { service.deleteUser(id) }
}