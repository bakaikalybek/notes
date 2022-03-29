package kg.bakai.notes.domain.repository

import kg.bakai.notes.domain.model.Note
import retrofit2.Call
import retrofit2.Response

interface NotesRepository {
    suspend fun getNotesList(): Response<List<Note>>
    suspend fun createNote(title: String, content: String): Response<Note>
    suspend fun updateNote(id: Int, title: String, content: String): Response<Note>
    suspend fun getNoteById(id: Int): Response<Note>
    suspend fun deleteNoteById(id: Int): Response<Unit>
}