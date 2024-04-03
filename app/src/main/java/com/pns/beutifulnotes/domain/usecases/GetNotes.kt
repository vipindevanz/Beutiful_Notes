package com.pns.beutifulnotes.domain.usecases

import com.pns.beutifulnotes.domain.model.Note
import com.pns.beutifulnotes.domain.repositories.NoteRepository
import kotlinx.coroutines.flow.Flow

/*
  Use cases must have one public function for accessibility
  Usecases are good, since they can be used by different viewModels in our app. Hence, we have to write less
  repetitive code in viewModels.
 */
class GetNotes(
    private val repository: NoteRepository
) {
    operator fun invoke(
    ): Flow<List<Note>> {
        return repository.getNotes()
    }
}