package com.pns.beutifulnotes.presentation.notes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pns.beutifulnotes.navigation.NavScreen
import com.pns.beutifulnotes.presentation.notes.components.NoteItemUI
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesScreen(
    navController: NavController,
    context: Context,
) {
    Scaffold(
        floatingActionButton = { FloatingButtonScaffold(navController = navController) },
        content = {
            ContentPartScaffold(
                navController = navController,
                context = context
            )
        }
    )

}

@Composable
fun FloatingButtonScaffold(navController: NavController) {
    FloatingActionButton(
        onClick = {
            navController.navigate(NavScreen.AddEditNoteScreen.route)
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
    }
}

@Composable
fun ContentPartScaffold(
    navController: NavController,
    context: Context,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Notes",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.notes) { note ->
                NoteItemUI(
                    note = note,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            //clicking on individual note
                            navController.navigate(
                                NavScreen.AddEditNoteScreen.route
                                        + "?noteId=${note.id}&noteColor=${note.color}"
                            )
                        },
                    onShareClicked = {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, note.content)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)

                    },
                    onDeleteClicked = {
                        viewModel.onEvent(NotesEvent.DeleteNote(note))
                        //after deleting the note, show the snackbar
                        scope.launch {
                            Log.d("message", "delete")
                            val result = snackbarHostState.showSnackbar(
                                message = "Note Deleted!",
                                actionLabel = "Undo"
                            )

                            when (result) {
                                SnackbarResult.ActionPerformed -> {
                                    viewModel.onEvent(NotesEvent.RestoreNote)

                                }

                                else -> {}
                            }

                        }
                    }
                )

                //adding space b/w each note Item
                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }


}