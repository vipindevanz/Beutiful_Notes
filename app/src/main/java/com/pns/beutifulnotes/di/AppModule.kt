package com.pns.beutifulnotes.di

import android.app.Application
import androidx.room.Room
import com.pns.beutifulnotes.data.data_source.NoteDB
import com.pns.beutifulnotes.data.repositories.NoteRepositoryImpl
import com.pns.beutifulnotes.domain.repositories.NoteRepository
import com.pns.beutifulnotes.domain.usecases.AddNote
import com.pns.beutifulnotes.domain.usecases.DeleteNote
import com.pns.beutifulnotes.domain.usecases.GetNote
import com.pns.beutifulnotes.domain.usecases.GetNotes
import com.pns.beutifulnotes.domain.usecases.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDB {
        return Room.databaseBuilder(
            app,
            NoteDB::class.java,
            NoteDB.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDB): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getIndividualNote = GetNote(repository)
        )
    }

}