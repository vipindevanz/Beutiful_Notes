package com.pns.beutifulnotes.presentation.notes

import com.pns.beutifulnotes.domain.model.Note

data class NotesState(
    val notes: List<Note> = emptyList(),
)
