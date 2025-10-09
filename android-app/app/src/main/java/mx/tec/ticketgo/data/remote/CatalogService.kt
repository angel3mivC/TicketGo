package mx.tec.ticketgo.data.remote

import mx.tec.ticketgo.data.models.CategoryResponse
import mx.tec.ticketgo.data.models.PriorityResponse
import mx.tec.ticketgo.data.models.RolesResponse
import mx.tec.ticketgo.data.models.StatesResponse
import retrofit2.Response
import retrofit2.http.GET

interface CatalogService {
    @GET("catalogs/roles")
    suspend fun getRoles(): Response<List<RolesResponse>>

    @GET("catalogs/estados")
    suspend fun getStates(): Response<List<StatesResponse>>

    @GET("catalogs/categorias")
    suspend fun getCategories(): Response<List<CategoryResponse>>

    @GET("catalogs/prioriaddes")
    suspend fun getPriorities(): Response<List<PriorityResponse>>
}