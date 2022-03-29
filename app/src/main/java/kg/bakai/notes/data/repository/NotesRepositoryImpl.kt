package kg.bakai.notes.data.repository

import kg.bakai.notes.data.network.NotesService
import kg.bakai.notes.domain.model.Note
import kg.bakai.notes.domain.repository.NotesRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val api: NotesService
): NotesRepository {

    override suspend fun getNotesList() = api.fetchNotes()

    override suspend fun createNote(title: String, content: String): Response<Note> {
        val jsonObjectString = JSONObject().apply {
            put("title", title)
            put("content", content)
        }.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        return api.insertNote(requestBody)
    }

    override suspend fun updateNote(id: Int, title: String, content: String): Response<Note> {
        val jsonObjectString = JSONObject().apply {
            put("title", title)
            put("content", content)
        }.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        return api.updateNote(id, requestBody)
    }

    override suspend fun getNoteById(id: Int): Response<Note> {
        return api.fetchNote(id)
    }

    override suspend fun deleteNoteById(id: Int): Response<Unit> {
        return api.deleteNote(id)
    }
}