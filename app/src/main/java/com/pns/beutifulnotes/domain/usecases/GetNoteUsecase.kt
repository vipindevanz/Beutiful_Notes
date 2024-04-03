package com.pns.beutifulnotes.domain.usecases

import com.pns.beutifulnotes.domain.model.Note
import com.pns.beutifulnotes.domain.repositories.NoteRepository

class GetNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}