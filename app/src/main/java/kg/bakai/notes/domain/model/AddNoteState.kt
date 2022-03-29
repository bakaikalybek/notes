package kg.bakai.notes.domain.model

data class AddNoteState(
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false,
    val note: Note? = null,
    val error: String = ""
)
