package kg.bakai.notes.data.network

import kg.bakai.notes.domain.model.Note
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface NotesService {
    @GET("api/note/")
    suspend fun fetchNotes(): Response<List<Note>>

    @POST("api/note/")
    suspend fun insertNote(@Body requestBody: RequestBody): Response<Note>

    @PUT("api/note/{id}/")
    suspend fun updateNote(@Path("id") id: Int, @Body requestBody: RequestBody): Response<Note>

    @GET("api/note/{id}")
    suspend fun fetchNote(@Path("id") id: Int): Response<Note>

    @DELETE("api/note/{id}/")
    suspend fun deleteNote(@Path("id") id: Int): Response<Unit>
}