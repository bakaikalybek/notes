package kg.bakai.notes.domain.model

data class NotesListState(
    val isLoading: Boolean = false,
    val data: List<Note> = emptyList(),
    val error: String = ""
)
