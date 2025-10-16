package mx.tec.ticketgo.data.remote

import mx.tec.ticketgo.data.models.Comment
import mx.tec.ticketgo.data.models.CreateCommentRequest
import mx.tec.ticketgo.data.models.CreateCommentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentService {
    @GET("tickets/{id}/comments/")
    suspend fun getComments(@Path("id") id:Int): Response<List<Comment>>

    @POST("tickets/{id}/comments/")
    suspend fun createComment(
        @Path("id") id:Int,
        @Body request: CreateCommentRequest
    ): Response<CreateCommentResponse>
}