package com.example.led_app.uiActivity


import android.annotation.SuppressLint
import android.net.Uri
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.led_app.application.LedAppFacade
import com.example.led_app.application.component.DaggerFacadeComponent
import com.example.led_app.domain.ConstantsString
import com.example.led_app.ui.theme.LED_APPTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch


//TODO DODAC INFORMACJE O WLACZENIU CZY WYLACZENIU LED
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

        composable(route = Screen.LedScreen.route,
            arguments = listOf(navArgument("ledIp") { type = NavType.StringType },
                navArgument("ledName") { type = NavType.StringType }
            )) { backStackEntry ->
            val ledIp = backStackEntry.arguments?.getString("ledIp") ?: ""
            val ledName = backStackEntry.arguments?.getString("ledName") ?: ""
            LedScreen(ledAppFacade = ledAppFacade, navController = navController, ledIp, ledName)
        }

        composable(route = Screen.ColorScreen.route,
            arguments = listOf(navArgument("ledIp") { type = NavType.StringType },
                navArgument("ledName") { type = NavType.StringType }
            )) { backStackEntry ->
            val ledIp = backStackEntry.arguments?.getString("ledIp") ?: ""
            val ledName = backStackEntry.arguments?.getString("ledName") ?: ""
            ColorScreen(ledAppFacade = ledAppFacade, navController = navController, ledIp, ledName)
        }

        composable(route = Screen.ChangeModeScreen.route) {
            ChangeModeScreen(ledAppFacade = ledAppFacade, navController = navController)
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
                showLedList(ledAppFacade, navController)
            }

            val isDialogVisible = remember { mutableStateOf(false) }
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
                    buttonText = ConstantsString.ADD_NEW_LED,
                    isVisible = isDialogVisible
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
private fun AddNewLedScreen(ledAppFacade: LedAppFacade, navController: NavHostController) {
    LED_APPTheme {
        var name by remember { mutableStateOf(TextFieldValue("")) }
        var address by remember { mutableStateOf(TextFieldValue("")) }
        Column {
            AppName(ConstantsString.APP_NAME)
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Box(
                modifier = Modifier
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(text = ConstantsString.LABEL_ADD_LED_NAME)
                Spacer(modifier = Modifier.height(30.dp))
                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = { Text(text = ConstantsString.LABEL_ADD_LED_NAME) },
                    placeholder = { Text(text = "Nazwa") },

                    )
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Box(
                modifier = Modifier
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(text = ConstantsString.LABEL_ADD_LED_IP)
                Spacer(modifier = Modifier.height(15.dp))
                TextField(
                    value = address,
                    onValueChange = {
                        address = it
                    },
                    label = { Text(text = ConstantsString.LABEL_ADD_LED_IP) },
                    placeholder = { Text(text = "127.0.0.1") },
                )
            }
        }
        val isDialogVisible = remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f),
            verticalArrangement = Arrangement.Bottom

        ) {
            ButtonToGoForward(
                onClick = {
                    coroutineScope.launch {
                        val isSaved = ledAppFacade.saveNewLed(name.text, address.text)
                        if (isSaved) {
                            navController.navigate(Screen.MainScreen.route)
                        } else {
                            isDialogVisible.value = true
                        }
                    }
                },
                buttonText = ConstantsString.BUTTON_ADD_NEW_LED,
                isVisible = isDialogVisible,
                dialogTitle = ConstantsString.DIALOG_TITLE_INFORMATION,
                dialogText = ConstantsString.LED_SERVER_NOT_RESPONSE
            )
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@Composable
private fun LedScreen(ledAppFacade: LedAppFacade, navController: NavHostController, ledIp: String, ledName: String) {
    val coroutineScope = rememberCoroutineScope()
    var dialogTextTurnOff = remember { mutableStateOf("") }
    val isTurnOffDialogVisible = remember { mutableStateOf(false) }
    val isLoaderVisible = remember { mutableStateOf(true) }

    LED_APPTheme {
        Column {
            AppName(ConstantsString.APP_NAME + " : " + ledName)
            Spacer(modifier = Modifier.height(45.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f),
                verticalArrangement = Arrangement.Top

            ) {
                ButtonToGoForward(
                    onClick = {
                        navController.navigate(
                            Screen.ColorScreen.route.replace(
                                oldValue = "{ledIp}",
                                newValue = Uri.encode(ledIp)
                            ).replace(
                                oldValue = "{ledName}",
                                newValue = ledName
                            )
                        )
                    },
                    buttonText = ConstantsString.BUTTON_SET_NEW_COLOR,
                    isEnable = !isLoaderVisible.value
                )
                Spacer(modifier = Modifier.height(30.dp))
                ButtonToGoForward(
                    onClick = {},
                    buttonText = ConstantsString.BUTTON_CHOSE_MODES,
                    isEnable = !isLoaderVisible.value
                )
                Spacer(modifier = Modifier.height(30.dp))
                ButtonToGoForward(
                    onClick = {},
                    buttonText = ConstantsString.BUTTON_UPDATE_DATA,
                    isEnable = !isLoaderVisible.value
                )
                Spacer(modifier = Modifier.height(30.dp))

                ButtonToGoForward(
                    onClick = {
                        coroutineScope.launch {
                            //TODO MAKE LOADER HERE
                            val turnOff = ledAppFacade.turnOffLed(ledIp)
                            isLoaderVisible.value = true
                            if (turnOff) {
                                dialogTextTurnOff.value = ConstantsString.LED_TURNED_OFF
                                isTurnOffDialogVisible.value = true
                                isLoaderVisible.value = false
                            } else {
                                dialogTextTurnOff.value = ConstantsString.ERROR_OCCURED
                                isTurnOffDialogVisible.value = true
                                isLoaderVisible.value = false
                            }
                        }
                    },
                    buttonText = ConstantsString.BUTTON_TURN_OFF_LED,
                    isVisible = isTurnOffDialogVisible,
                    dialogTitle = ConstantsString.DIALOG_TITLE_INFORMATION,
                    dialogText = dialogTextTurnOff.value,
                    isEnable = !isLoaderVisible.value
                )
                // Call Loader
                if (isLoaderVisible.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp).height(64.dp).align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

        }
    }
}

//TODO SCREEN TO REFACTOR
@Composable
private fun ColorScreen(ledAppFacade: LedAppFacade, navController: NavHostController, ledIp: String, ledName: String) {
    var redValue = 0
    var greenValue = 0
    var blueValue = 0
    LED_APPTheme {
        Column {
            AppName(ConstantsString.APP_NAME + " : " + ledName)
            Spacer(modifier = Modifier.height(45.dp))

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var redValueBar by remember { mutableStateOf(100) }
                var greenValueBar by remember { mutableStateOf(100) }
                var blueValueBar by remember { mutableStateOf(100) }
                var brightness by remember { mutableStateOf(0) }

                Text(text = "Ustaw Kolor:", textAlign = TextAlign.Left, fontSize = 25.sp)
                Spacer(modifier = Modifier.height(40.dp))

                AddSliderWithText("Czerwony:", 0f, 255f, redValueBar) { newRed ->
                    redValueBar = newRed
                }


                Spacer(modifier = Modifier.height(30.dp))
                AddSliderWithText("Niebieski:", 0f, 255f, blueValueBar) { newBlue ->
                    blueValueBar = newBlue
                }

                Spacer(modifier = Modifier.height(30.dp))
                AddSliderWithText("Zielony:", 0f, 255f, greenValueBar) { newGreen ->
                    greenValueBar = newGreen
                }

                Spacer(modifier = Modifier.height(30.dp))
                AddSliderWithText("Jasność:", -25f, 25f, brightness) { newBrightness ->
                    brightness = newBrightness
                }

                Spacer(modifier = Modifier.height(16.dp))


                redValue = checkColorValue(redValueBar + brightness)
                greenValue = checkColorValue(greenValueBar + brightness)
                blueValue = checkColorValue(blueValueBar + brightness)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(60.dp)
                        .background(
                            Color(
                                redValue,
                                greenValue,
                                blueValue
                            )
                        )
                )

                Spacer(modifier = Modifier.height(16.dp))
                ButtonToGoForward(
                    onClick = {
                        navController.navigate(Screen.ChangeModeScreen.route)
                    },
                    buttonText = ConstantsString.BUTTON_CHOSE_CHANGE_MODE,
                )
            }
        }
    }
}


@Composable
private fun ChangeModeScreen(ledAppFacade: LedAppFacade, navController: NavHostController) {
    LED_APPTheme {
        Column {
            AppName(ConstantsString.APP_NAME)
            Spacer(modifier = Modifier.height(45.dp))
        }
    }
}

@Composable
fun AddSliderWithText(text: String, min: Float, max: Float, currentValue: Int, value: (Int) -> Unit) {

    Text(text = text, textAlign = TextAlign.Left, fontSize = 18.sp)
    Slider(
        value = currentValue.toFloat(),
        onValueChange = { value(it.toInt()) },
        valueRange = min..max,
        steps = Math.abs(max - min).toInt()
    )
}


@Composable
fun showLedList(ledAppFacade: LedAppFacade, navController: NavHostController) {
    val serverList = ledAppFacade.getAllServersNameAndAddress()
    LazyColumn(Modifier.fillMaxHeight()) {
        items(serverList) { pair ->
            AddLedButton(
                text = ConstantsString.SERVER_NAME + " ".repeat(5) + pair.first,
                ledAddress = pair.second,
                ledName = pair.first,
                ledAppFacade,
                navController
            )
        }
    }
}


@Composable
fun AddLedButton(
    text: String,
    ledAddress: String,
    ledName: String,
    ledAppFacade: LedAppFacade,
    navController: NavHostController
) {
    val isDialogVisible = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    return Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    val connected = ledAppFacade.testConnectionWithServer(ledAddress)
                    if (connected) {
                        navController.navigate(
                            Screen.LedScreen.route.replace(
                                oldValue = "{ledIp}",
                                newValue = Uri.encode(ledAddress)
                            ).replace(
                                oldValue = "{ledName}",
                                newValue = ledName
                            )
                        )
                    } else {
                        isDialogVisible.value = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(60.dp)
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
    buttonText: String,
    dialogText: String? = "Allert",
    dialogTitle: String? = "Allert",
    isVisible: MutableState<Boolean>? = null,
    isEnable: Boolean? = true,
) {
    Button(
        onClick = onClick,
        enabled = isEnable!!,
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .border(2.dp, MaterialTheme.colorScheme.background, shape = RectangleShape)
    ) {
        Text(text = buttonText, textAlign = TextAlign.Center, fontSize = 18.sp)
        if (isVisible != null && isVisible.value) {
            AlertDialog(
                onDismissRequest = { isVisible.value = false },
                onConfirmation = { isVisible.value = false },
                dialogText = dialogText!!,
                dialogTitle = dialogTitle!!
            )
        }
    }

}

fun checkColorValue(newColorValue: Int): Int {
    if (newColorValue > 255) {
        return 255
    }
    if (newColorValue < 0) {
        return 0
    }
    return newColorValue
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

