package com.example.tasksapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasksapp.ui.theme.TasksAppTheme

data class Task(val text: String, val completed: MutableState<Boolean> = mutableStateOf(false))
private fun Button.setOnClickListener(onClick: () -> Unit   , modifier: Modifier) {

}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        setContent {
            TasksAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun TaskDialog(onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Create a new task") },
        confirmButton = {
            Button(
                onClick = onClose
            ) {
                Text("Close")
            }
        },
        text = { /* Task Creation components go here */ }
    )
}

@Composable
fun IntroPage(navController: NavHostController) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tasks App",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    color = Color.Gray
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate("tasksMainPage") },
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(text = "Enter")
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "intro") {
        composable("intro") {
            IntroPage(navController)
        }
        composable("tasksMainPage") {
            TasksMainPage(navController)
        }
    }
}

@SuppressLint("QueryPermissionsNeeded")
@Composable
fun TasksMainPage(navController: NavHostController) {
    var showDialogBox by remember { mutableStateOf(false) }
    var completeDialogBox by remember { mutableStateOf(false) }
    var tasks by remember { mutableStateOf(emptyList<Task>()) }
    val context = LocalContext.current
    var completedTaskText by remember { mutableStateOf("") }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            //Header
            Column(
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Text(
                    text = "Current Tasks",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        fontSize = 48.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                // Center, Middle part
                Column {
                    tasks.forEach { task ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = task.completed.value,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        completedTaskText = task.text
                                        task.completed.value = isChecked
                                        completeDialogBox = true
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (task.completed.value) "✅" else "❌",
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = task.text,
                                fontSize = 20.sp
                            )

                        }
                    }
                }
            }
            // Footer
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { showDialogBox = true },
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Text(text = "New Task")
                }
            }
            if (completeDialogBox) {
                CompletionDialog(
                    taskName = completedTaskText,
                    onYes = {
                        val emailSubject = "Following task completed: $completedTaskText"
                        val intent = Intent(Intent.ACTION_SENDTO)
                        val resolvedActivity = intent.resolveActivity(context.packageManager)
                        Log.d("ResolvedActivity", "Resolved Activity: $resolvedActivity")
                        intent.data = Uri.parse("mailto:")
                        intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                        intent.putExtra(Intent.EXTRA_TEXT, "")
                        intent.`package` = "com.microsoft.office.outlook"

                        // Remove completed tasks
                        tasks = tasks.filter { !it.completed.value }
                        completeDialogBox = false // Dismiss the dialog
                        if (resolvedActivity == null) {
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(context, "Microsoft Outlook is not installed.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onNo = {
                        // Remove completed tasks
                        tasks = tasks.filter { !it.completed.value }
                        completeDialogBox = false // Dismiss the dialog
                    },
                    onCancel = {
                        tasks.forEach { task ->
                            if (task.completed.value) {
                                task.completed.value = false
                            }
                        }
                        completeDialogBox = false // Dismiss the dialog
                    }
                )
            }
            TaskDialog(
                showDialog = showDialogBox,
                onDismiss = { showDialogBox = false},
                onConfirm = { taskText ->
                    tasks = tasks + Task(taskText)
                    showDialogBox = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TasksAppTheme {
        AppNavigation()
    }
}