package com.example.led_app.uiActivity


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.led_app.application.component.DaggerFacadeComponent
import com.example.led_app.domain.ConstantsString
import com.example.led_app.domain.LedAppFacade
import com.example.led_app.ui.theme.LED_APPTheme

class MainActivity : ComponentActivity() {

    private val ledAppFacade: LedAppFacade = DaggerFacadeComponent.create().injectFacade()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation(ledAppFacade)
        }
    }
}

@Composable
fun Navigation(ledAppFacade: LedAppFacade) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(ledAppFacade = ledAppFacade, navController = navController)
        }
        composable(route = Screen.AddNewLedScreen.route) {
            AddNewLedScreen(ledAppFacade = ledAppFacade, navController = navController)
        }
    }
}


@Composable
private fun MainScreen(ledAppFacade: LedAppFacade, navController: NavHostController) {
    LED_APPTheme {
        Column {
            AppName(ConstantsString.APP_NAME)
            Spacer(modifier = Modifier.height(15.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
            ) {
                Text(
                    text = ConstantsString.CHOSE_SERVER, style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp, fontWeight = FontWeight.Bold
                    ), textAlign = TextAlign.Left, modifier = Modifier.padding(6.dp)

                )
                Spacer(modifier = Modifier.height(15.dp))
                showLedList(ledAppFacade)
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f),
                verticalArrangement = Arrangement.Bottom

            ) {
                ButtonToGoForward(
                    onClick = {
                        navController.navigate(Screen.AddNewLedScreen.route)
                    },
                    buttonText = ConstantsString.ADD_NEW_LED
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddNewLedScreen(ledAppFacade: LedAppFacade, navController: NavHostController) {
    LED_APPTheme {
        var name by remember { mutableStateOf(TextFieldValue("")) }
        var address by remember { mutableStateOf(TextFieldValue("")) }
        Column {
            AppName(ConstantsString.APP_NAME)
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            TextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = { Text(text = ConstantsString.LABEL_ADD_LED_NAME) },
                placeholder = { Text(text = "Nazwa") },
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            TextField(
                value = address,
                onValueChange = {
                    address = it
                },
                label = { Text(text = ConstantsString.LABEL_ADD_LED_IP) },
                placeholder = { Text(text = "127.0.0.1") },
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f),
            verticalArrangement = Arrangement.Bottom

        ) {
            ButtonToGoForward(
                onClick = {
                    val isSaved = ledAppFacade.saveNewLed(name.text, address.text)
                    if (isSaved) {
                        navController.navigate(Screen.MainScreen.route)
                    }
                },
                buttonText = ConstantsString.BUTTON_ADD_NEW_LED
            )
        }
    }
}

@Composable
fun showLedList(ledAppFacade: LedAppFacade) {
    val serverList = ledAppFacade.getAllServersName()
    LazyColumn(Modifier.fillMaxHeight()) {
        items(serverList) { ledName ->
            AddLedButton(text = ConstantsString.SERVER_NAME + " ".repeat(10) + ledName)
        }
    }
}


@Composable
fun AddLedButton(text: String) {
    val isDialogVisible = remember { mutableStateOf(false) }
    return Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Button(
            onClick = {
                isDialogVisible.value = true
            },
            modifier = Modifier.fillMaxWidth().height(40.dp)
                .border(2.dp, MaterialTheme.colorScheme.background, shape = RectangleShape),
            colors = ButtonDefaults.buttonColors(Color.DarkGray)


        ) {
            Text(text = text, textAlign = TextAlign.Left)
            if (isDialogVisible.value) {
                AlertDialog(
                    onDismissRequest = { isDialogVisible.value = false },
                    onConfirmation = { isDialogVisible.value = false },
                    dialogText = ConstantsString.DIALOG_INFORMATION_LED_NOT_EXIST,
                    dialogTitle = ConstantsString.DIALOG_TITLE_INFORMATION
                )
            }
        }
    }
}

@Composable
fun AppName(appName: String) {

    Box(
        modifier = Modifier.fillMaxWidth().height(70.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, shape = RectangleShape)
            .clip(MaterialTheme.shapes.medium)
            .background(Color.LightGray), contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = appName, style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp, fontWeight = FontWeight.ExtraBold
            ), textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp)

        )
    }
}

@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
            ) {
                Text("OK")
            }
        }
    )
}


@Composable
fun ButtonToGoForward(
    onClick: () -> Unit,
    buttonText: String
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(2.dp, MaterialTheme.colorScheme.background, shape = RectangleShape)
    ) {
        Text(text = buttonText, textAlign = TextAlign.Center)
    }

}


//@Composable
//fun Conversation(messages: List<Message>) {
//    LazyColumn {
//        items(messages) { message ->
//            Greeting(message)
//        }
//    }
//}

//@Composable
//fun Greeting(messages: Message) {
//    Row {
//        Image(
//            painter = painterResource(R.drawable.calculate_matrix),
//            contentDescription = "Contact profile picture",
//            modifier = Modifier
//                .size(40.dp)
//                .clip(CircleShape)
//                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Column {
//            Text(
//                text = messages.author,
//                color = MaterialTheme.colorScheme.secondary,
//                style = MaterialTheme.typography.bodyMedium,
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Surface(shape = MaterialTheme.shapes.medium, shadowElevation = 1.dp) {
//                Text(
//                    text = messages.body,
//                    style = MaterialTheme.typography.bodyMedium,
//                    modifier = Modifier.padding(all = 4.dp)
//                )
//            }
//
//        }
//
//    }
//}

