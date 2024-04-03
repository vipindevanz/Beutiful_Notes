package com.pns.beutifulnotes.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pns.beutifulnotes.domain.model.Note
import com.pns.beutifulnotes.domain.usecases.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {
    //We need to have one state wrapper class that represents the current UI state of the note screen
    //State will have 4 things : current note order, current list of notes, Restore notes and
    // order section visibility

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentDeletedNote: Note? = null

    /* We will get new instance of flow, when we call getNotes, so we have to cancel the old coroutine
       observing our database  */

    private var getNotesJob: Job? = null

    init {
        getNotes()
    }

    fun onEvent(event: NotesEvent) {
        when (event) {

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentDeletedNote = event.note
                }

            }

            is NotesEvent.RestoreNote -> {
                //keep reference of last deleted note
                viewModelScope.launch {
                    noteUseCases.addNote(recentDeletedNote ?: return@launch)

                    //null bcz if user call multiple times, same note can't be inserted again
                    recentDeletedNote = null
                }
            }
        }
    }

    private fun getNotes() {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes().onEach { notes ->
            _state.value = state.value.copy(
                notes = notes,
            )
        }.launchIn(viewModelScope)
    }
}