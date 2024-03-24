package com.example.tasksapp

import android.graphics.fonts.FontStyle
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasksapp.ui.theme.TasksAppTheme

data class Task(val text: String, var completed: Boolean = false)
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

@Composable
fun TasksMainPage(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    var tasks by remember { mutableStateOf(emptyList<Task>()) }

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
                                checked = task.completed,
                                onCheckedChange = { isChecked ->
                                    task.completed = isChecked
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = task.text)
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
                    onClick = { showDialog = true },
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Text(text = "New Task")
                }
            }

            TaskDialog(
                showDialog = showDialog,
                onDismiss = { showDialog = false},
                onConfirm = { taskText ->
                    tasks = tasks + Task(taskText)
                    showDialog = false
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