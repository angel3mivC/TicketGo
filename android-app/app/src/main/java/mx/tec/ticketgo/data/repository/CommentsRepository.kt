package mx.tec.ticketgo.data.repository

import mx.tec.ticketgo.data.models.Comment
import mx.tec.ticketgo.data.models.CreateCommentRequest
import mx.tec.ticketgo.data.models.CreateCommentResponse
import mx.tec.ticketgo.data.network.ApiClient
import mx.tec.ticketgo.data.remote.CommentService
import mx.tec.ticketgo.data.utils.safeApiCall

class CommentsRepository {
    private val service: CommentService = ApiClient.retrofit.create(CommentService::class.java)

    suspend fun getComments(id: Int): Result<List<Comment>> =
        safeApiCall { service.getComments(id) }

    suspend fun createComment(id: Int, request: CreateCommentRequest): Result<CreateCommentResponse> =
        safeApiCall { service.createComment(id, request) }
}