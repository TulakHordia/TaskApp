package com.example.tasksapp

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CompletionDialog (
    taskName: String,
    onYes: () -> Unit,
    onNo: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(" Send completion email?") },
        confirmButton = {
            Button(onClick = onYes) {
                Text("Send")
            }
        },
        dismissButton = {
            Button(onClick = onNo) {
                Text("Delete Task")
            }
        },
    )
}
