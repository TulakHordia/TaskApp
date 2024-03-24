package com.example.tasksapp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun TaskDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var taskText by remember { mutableStateOf(TextFieldValue()) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Add new Task") },
            text = { Column {
                Text(text = "Enter your new task:")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = taskText,
                    onValueChange = { taskText = it },
                    modifier = Modifier.fillMaxWidth()
                )
                } },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm(taskText.text)
                        taskText = TextFieldValue() //Clearing the text
                        onDismiss() //Closes the dialog

                    }
                ) {
                    Text(text = "Add")
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss
                ) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}

