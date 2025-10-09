package mx.tec.ticketgo.data.repository

import mx.tec.ticketgo.data.models.CategoryResponse
import mx.tec.ticketgo.data.models.PriorityResponse
import mx.tec.ticketgo.data.models.RolesResponse
import mx.tec.ticketgo.data.models.StatesResponse
import mx.tec.ticketgo.data.network.ApiClient
import mx.tec.ticketgo.data.remote.CatalogService
import mx.tec.ticketgo.data.utils.safeApiCall

class CatalogRepository() {
    private val service: CatalogService = ApiClient.retrofit.create(CatalogService::class.java)

    suspend fun getRoles(): Result<List<RolesResponse>> =
        safeApiCall { service.getRoles() }

    suspend fun getStates(): Result<List<StatesResponse>> =
        safeApiCall { service.getStates() }

    suspend fun getCategories(): Result<List<CategoryResponse>> =
        safeApiCall { service.getCategories() }

    suspend fun getPriorities(): Result<List<PriorityResponse>> =
        safeApiCall { service.getPriorities() }
}