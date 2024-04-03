package com.pns.beutifulnotes.domain.usecases

import com.pns.beutifulnotes.domain.model.Note
import com.pns.beutifulnotes.domain.repositories.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}