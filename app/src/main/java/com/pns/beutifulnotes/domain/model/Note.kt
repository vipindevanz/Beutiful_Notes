package com.pns.beutifulnotes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pns.beutifulnotes.ui.theme.BabyBlue
import com.pns.beutifulnotes.ui.theme.LightGreen
import com.pns.beutifulnotes.ui.theme.RedOrange
import com.pns.beutifulnotes.ui.theme.RedPink
import com.pns.beutifulnotes.ui.theme.Violet

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(message: String) : Exception(message)