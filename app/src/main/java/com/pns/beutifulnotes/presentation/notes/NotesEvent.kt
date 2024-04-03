package com.pns.beutifulnotes.presentation.notes

import com.pns.beutifulnotes.domain.model.Note

sealed class NotesEvent {
    data class DeleteNote(val note: Note) : NotesEvent()
    object RestoreNote : NotesEvent()
}
