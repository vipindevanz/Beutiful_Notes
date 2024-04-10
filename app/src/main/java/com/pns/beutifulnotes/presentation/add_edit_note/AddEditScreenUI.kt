package com.pns.beutifulnotes.presentation.add_edit_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pns.beutifulnotes.presentation.add_edit_note.components.HintUI
import com.pns.beutifulnotes.domain.model.Note
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditScreenUI(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val noteBgAnimation = remember {
        Animatable(
            Color(
                if (noteColor != -1) noteColor else viewModel.noteColor.value
            )
        )
    }

    // To animate the above color, we need a scope
    val scope = rememberCoroutineScope()

    // Getting all the latest events
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    SnackbarHostState().showSnackbar(
                        message = event.message
                    )
                }

                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }

                else -> {

                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            //fire an event to ViewModel to save a note, during onClick operation
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save Note")
            }
        },
        content = { padding ->
            //making a Row to select colors
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(noteBgAnimation.value)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //make circles for each color we have
                    Note.noteColors.forEach { color ->
                        val colorInt = color.toArgb()

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .shadow(15.dp, CircleShape)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = 4.dp,
                                    color = if (viewModel.noteColor.value == colorInt) {
                                        Color.Black  //color is selected
                                    } else {
                                        Color.Transparent  //color is deselected
                                    },
                                    shape = CircleShape
                                )
                                .clickable {
                                    scope.launch {
                                        noteBgAnimation.animateTo(
                                            targetValue = Color(colorInt),
                                            animationSpec = tween(durationMillis = 500)
                                        )
                                    }

                                    viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                                }

                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // For Title
                HintUI(
                    text = titleState.text,
                    hint = titleState.hint,
                    isHintVisible = titleState.isHintVisible,
                    onValueChange = {
                        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    singleLine = true
                ) {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                }

                Spacer(Modifier.height(16.dp))

                // For Content
                HintUI(
                    text = contentState.text,
                    hint = contentState.hint,
                    isHintVisible = contentState.isHintVisible,
                    onValueChange = {
                        viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                    },
                    textStyle = MaterialTheme.typography.bodyMedium
                ) {
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                }
            }
        }
    )
}
