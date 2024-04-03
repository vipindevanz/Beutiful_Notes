package com.pns.beutifulnotes.domain.repositories

import com.pns.beutifulnotes.domain.model.Note
import kotlinx.coroutines.flow.Flow

//we declared an interface for repository, so as to make testing easier
interface NoteRepository {

    fun getNotes(): Flow<List<Note>>

    suspend fun getNoteById(id: Int): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)
}