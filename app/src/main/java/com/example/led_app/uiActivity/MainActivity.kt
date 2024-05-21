package com.example.led_app.uiActivity


import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.led_app.application.LedAppFacade
import com.example.led_app.application.component.DaggerFacadeComponent
import com.example.led_app.application.module.FacadeModule
import com.example.led_app.domain.ChangeModeData
import com.example.led_app.domain.ConstantsString
import com.example.led_app.domain.LedModeData
import com.example.led_app.domain.NewServerRequest
import com.example.led_app.ui.theme.LED_APPTheme
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {

    private lateinit var ledAppFacade: LedAppFacade
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ledAppFacade = DaggerFacadeComponent.builder().facadeModule(FacadeModule(this)).build().injectFacade()
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

        composable(
            route = Screen.ColorScreen.route,
            arguments = listOf(navArgument("requestBuilder") {
                type = NewServerRequest.BuilderType()
            })
        ) { backStackEntry ->
            val requestBuilder = backStackEntry.arguments?.getParcelable<NewServerRequest.Builder>("requestBuilder")
            ColorScreen(navController = navController, requestBuilder!!)
        }

        composable(
            route = Screen.ChangeModeScreen.route,
            arguments = listOf(navArgument("requestBuilder") {
                type = NewServerRequest.BuilderType()
            })
        ) { backStackEntry ->
            val requestBuilder = backStackEntry.arguments?.getParcelable<NewServerRequest.Builder>("requestBuilder")
            ChangeModeScreen(ledAppFacade = ledAppFacade, navController = navController, requestBuilder!!)
        }

        composable(
            route = Screen.LedModeScreen.route,
            arguments = listOf(navArgument("requestBuilder") {
                type = NewServerRequest.BuilderType()
            })
        ) { backStackEntry ->
            val requestBuilder = backStackEntry.arguments?.getParcelable<NewServerRequest.Builder>("requestBuilder")
            LedModeScreen(ledAppFacade = ledAppFacade, navController = navController, requestBuilder!!)
        }

        composable(
            route = Screen.LedModeColorScreen.route,
            arguments = listOf(navArgument("requestBuilder") {
                type = NewServerRequest.BuilderType()
            })
        ) { backStackEntry ->
            val requestBuilder = backStackEntry.arguments?.getParcelable<NewServerRequest.Builder>("requestBuilder")
            LedModeColorScreen(ledAppFacade = ledAppFacade, navController = navController, requestBuilder!!)
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
        val isLoaderVisible = remember { mutableStateOf(false) }
        var dialogText = remember { mutableStateOf("") }
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
                        isLoaderVisible.value = true
                        val isSaved = withContext(Dispatchers.IO) {
                            ledAppFacade.saveNewLed(name.text, address.text)
                        }
                        isLoaderVisible.value = false
                        if (isSaved.first) {
                            navController.navigate(Screen.MainScreen.route)
                        } else {
                            dialogText.value = isSaved.second
                            isDialogVisible.value = true
                        }

                    }

                },
                buttonText = ConstantsString.BUTTON_ADD_NEW_LED,
                isVisible = isDialogVisible,
                dialogTitle = ConstantsString.DIALOG_TITLE_INFORMATION,
                dialogText = dialogText.value,
                isEnable = !isLoaderVisible.value
            )

            if (isLoaderVisible.value) {
                CircularProgressBar()
            }
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@Composable
private fun LedScreen(ledAppFacade: LedAppFacade, navController: NavHostController, ledIp: String, ledName: String) {
    val coroutineScope = rememberCoroutineScope()
    var dialogText = remember { mutableStateOf("") }
    val isDialogVisible = remember { mutableStateOf(false) }
    val isLoaderVisible = remember { mutableStateOf(false) }
    val requestBuilder =
        NewServerRequest.Builder("", "", 0, 0, 0, 0)
            .withLedName(ledName).withLedIp(ledIp)

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
                        val jsonRequestBuilder = Uri.encode(Gson().toJson(requestBuilder))
                        navController.navigate(
                            Screen.ColorScreen.route.replace(
                                oldValue = "{requestBuilder}",
                                newValue = jsonRequestBuilder
                            )
                        )
                    },
                    buttonText = ConstantsString.BUTTON_SET_NEW_COLOR,
                    isEnable = !isLoaderVisible.value
                )
                Spacer(modifier = Modifier.height(30.dp))
                ButtonToGoForward(
                    onClick = {
                        val jsonRequestBuilder = Uri.encode(Gson().toJson(requestBuilder))
                        navController.navigate(
                            Screen.LedModeScreen.route.replace(
                                oldValue = "{requestBuilder}",
                                newValue = jsonRequestBuilder
                            )
                        )
                    },
                    buttonText = ConstantsString.BUTTON_CHOSE_MODES,
                    isEnable = !isLoaderVisible.value
                )
                Spacer(modifier = Modifier.height(30.dp))
                ButtonToGoForward(
                    onClick = {
                        coroutineScope.launch {
                            isLoaderVisible.value = true
                            val isUpdated = withContext(Dispatchers.IO) {
                                ledAppFacade.updateLedConfig(ledName, ledIp)
                            }
                            isLoaderVisible.value = false
                            if (isUpdated) {
                                isDialogVisible.value = true
                                dialogText.value = ConstantsString.LED_UPDATED
                            } else {
                                isDialogVisible.value = true
                                dialogText.value = ConstantsString.ERROR_OCCURED
                            }
                        }
                    },
                    buttonText = ConstantsString.BUTTON_UPDATE_DATA,
                    isEnable = !isLoaderVisible.value,
                    dialogTitle = ConstantsString.DIALOG_TITLE_INFORMATION,
                    dialogText = dialogText.value,
                    isVisible = isDialogVisible,
                )
                Spacer(modifier = Modifier.height(30.dp))

                ButtonToGoForward(
                    onClick = {
                        coroutineScope.launch {
                            isLoaderVisible.value = true
                            val turnOff = ledAppFacade.turnOffLed(ledIp)
                            isLoaderVisible.value = false
                            if (turnOff) {
                                dialogText.value = ConstantsString.LED_TURNED_OFF
                                isDialogVisible.value = true

                            } else {
                                dialogText.value = ConstantsString.ERROR_OCCURED
                                isDialogVisible.value = true
                            }
                        }
                    },
                    buttonText = ConstantsString.BUTTON_TURN_OFF_LED,
                    isVisible = isDialogVisible,
                    dialogTitle = ConstantsString.DIALOG_TITLE_INFORMATION,
                    dialogText = dialogText.value,
                    isEnable = !isLoaderVisible.value
                )

                Spacer(modifier = Modifier.height(30.dp))

                ButtonToGoForward(
                    onClick = {
                        coroutineScope.launch {
                            isLoaderVisible.value = true
                            val isDeleted = withContext(Dispatchers.IO) {
                                ledAppFacade.deleteLedByName(ledName)
                            }
                            isLoaderVisible.value = false
                            if (isDeleted) {
                                dialogText.value = ConstantsString.LED_DELETED
                                isDialogVisible.value = true
                                navController.navigate(Screen.MainScreen.route)

                            } else {
                                dialogText.value = ConstantsString.ERROR_OCCURED
                                isDialogVisible.value = true
                            }
                        }
                    },
                    buttonText = ConstantsString.BUTTON_DELETED_LED,
                    isVisible = isDialogVisible,
                    dialogTitle = ConstantsString.DIALOG_TITLE_INFORMATION,
                    dialogText = dialogText.value,
                    isEnable = !isLoaderVisible.value
                )

                if (isLoaderVisible.value) {
                    CircularProgressBar()
                }
            }

        }
    }
}

@Composable
private fun ColorScreen(navController: NavHostController, requestBuilder: NewServerRequest.Builder) {
    var redValue = 0
    var greenValue = 0
    var blueValue = 0
    LED_APPTheme {
        Column {
            AppName(ConstantsString.APP_NAME + " : " + requestBuilder.getLedName())
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
                        requestBuilder.withRedValue(redValue)
                            .withGreenValue(greenValue).withBlueValue(blueValue)

                        val jsonRequestBuilder = Uri.encode(Gson().toJson(requestBuilder))

                        navController.navigate(
                            Screen.ChangeModeScreen.route.replace(
                                oldValue = "{requestBuilder}",
                                newValue = jsonRequestBuilder
                            )
                        )
                    },
                    buttonText = ConstantsString.BUTTON_CHOSE_CHANGE_MODE,
                )
            }
        }
    }
}


@Composable
private fun ChangeModeScreen(
    ledAppFacade: LedAppFacade,
    navController: NavHostController,
    requestBuilder: NewServerRequest.Builder
) {
    var changesModeList by remember {
        mutableStateOf<List<ChangeModeData>>(emptyList())
    }

    LaunchedEffect(key1 = true) {
        changesModeList = withContext(Dispatchers.IO) {
            ledAppFacade.getChangesModeByName(requestBuilder.getLedName())
        }
    }

    LED_APPTheme {
        Column {
            val coroutineScope = rememberCoroutineScope()
            var selectedMode: ChangeModeData? by remember { mutableStateOf(null) }
            var dialogTextSendRequest = remember { mutableStateOf("") }
            val isRequestDialogVisible = remember { mutableStateOf(false) }
            val isLoaderVisible = remember { mutableStateOf(false) }

            AppName(ConstantsString.APP_NAME + " : " + requestBuilder.getLedName())
            Spacer(modifier = Modifier.height(45.dp))
            LazyColumn {
                items(changesModeList) { changeMode ->
                    CheckboxTileItem(
                        tile = changeMode.optionName,
                        isSelected = changeMode == selectedMode,
                        onSelectedChange = {
                            selectedMode = changeMode
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(45.dp))

            ButtonToGoForward(
                onClick = {

                    if (selectedMode != null) {
                        isRequestDialogVisible.value = false
                        val serverRequest: NewServerRequest =
                            requestBuilder.withChangeModeServerId(selectedMode?.changeModeServerId!!).build()
                        coroutineScope.launch {
                            isLoaderVisible.value = true
                            val sentSuccessful = ledAppFacade.sendColorRequest(serverRequest)
                            isLoaderVisible.value = false
                            if (sentSuccessful) {
                                navController.navigate(
                                    Screen.LedScreen.route.replace(
                                        oldValue = "{ledIp}",
                                        newValue = serverRequest.ledIp
                                    ).replace(
                                        oldValue = "{ledName}",
                                        newValue = serverRequest.ledName
                                    )
                                )
                            } else {
                                dialogTextSendRequest.value = ConstantsString.SEND_ERROR_OCCURED
                                isRequestDialogVisible.value = true
                            }
                        }

                    } else {
                        dialogTextSendRequest.value = ConstantsString.CHANGE_OPTION_NEEDED_TO_CHOOSE
                        isRequestDialogVisible.value = true
                    }
                },
                buttonText = ConstantsString.SEND_REQUEST,
                isVisible = isRequestDialogVisible,
                dialogTitle = ConstantsString.DIALOG_TITLE_INFORMATION,
                dialogText = dialogTextSendRequest.value,
                isEnable = !isLoaderVisible.value
            )

        }
    }
}


@Composable
private fun LedModeScreen(
    ledAppFacade: LedAppFacade,
    navController: NavHostController,
    requestBuilder: NewServerRequest.Builder
) {
    var ledModeList by remember {
        mutableStateOf<List<LedModeData>>(emptyList())
    }

    LaunchedEffect(key1 = true) {
        ledModeList = withContext(Dispatchers.IO) {
            ledAppFacade.getLedModeByName(requestBuilder.getLedName())
        }
    }

    LED_APPTheme {
        Column {
            val coroutineScope = rememberCoroutineScope()
            var selectedMode: LedModeData? by remember { mutableStateOf(null) }
            var dialogTextSendRequest = remember { mutableStateOf("") }
            val isRequestDialogVisible = remember { mutableStateOf(false) }
            val isLoaderVisible = remember { mutableStateOf(false) }

            AppName(ConstantsString.APP_NAME + " : " + requestBuilder.getLedName())
            Spacer(modifier = Modifier.height(45.dp))
            LazyColumn {
                items(ledModeList) { mode ->
                    CheckboxTileItem(
                        tile = mode.optionName,
                        isSelected = mode == selectedMode,
                        onSelectedChange = {
                            selectedMode = mode
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(45.dp))

            ButtonToGoForward(
                onClick = {

                    if (selectedMode != null) {
                        isRequestDialogVisible.value = false
                        requestBuilder.withChangeModeServerId(selectedMode?.modeServerId!!)

                        if (selectedMode?.setColor!!) {
                            val jsonRequestBuilder = Uri.encode(Gson().toJson(requestBuilder))
                            navController.navigate(
                                Screen.LedModeColorScreen.route.replace(
                                    oldValue = "{requestBuilder}",
                                    newValue = jsonRequestBuilder
                                )
                            )
                        } else {
                            val serverRequest: NewServerRequest = requestBuilder.build()

                            coroutineScope.launch {
                                isLoaderVisible.value = true
                                val sentSuccessful = ledAppFacade.sendModeRequest(serverRequest)
                                isLoaderVisible.value = false
                                if (sentSuccessful) {
                                    navController.navigate(
                                        Screen.LedScreen.route.replace(
                                            oldValue = "{ledIp}",
                                            newValue = serverRequest.ledIp
                                        ).replace(
                                            oldValue = "{ledName}",
                                            newValue = serverRequest.ledName
                                        )
                                    )
                                } else {
                                    dialogTextSendRequest.value = ConstantsString.SEND_ERROR_OCCURED
                                    isRequestDialogVisible.value = true
                                }
                            }
                        }

                    } else {
                        dialogTextSendRequest.value = ConstantsString.CHANGE_OPTION_NEEDED_TO_CHOOSE
                        isRequestDialogVisible.value = true
                    }
                },
                buttonText = ConstantsString.SEND_REQUEST,
                isVisible = isRequestDialogVisible,
                dialogTitle = ConstantsString.DIALOG_TITLE_INFORMATION,
                dialogText = dialogTextSendRequest.value,
                isEnable = !isLoaderVisible.value
            )

        }
    }
}

@Composable
private fun LedModeColorScreen(
    ledAppFacade: LedAppFacade,
    navController: NavHostController,
    requestBuilder: NewServerRequest.Builder
) {
    var redValue = 0
    var greenValue = 0
    var blueValue = 0
    LED_APPTheme {
        Column {
            val coroutineScope = rememberCoroutineScope()
            var dialogTextSendRequest = remember { mutableStateOf("") }
            val isRequestDialogVisible = remember { mutableStateOf(false) }
            val isLoaderVisible = remember { mutableStateOf(false) }

            AppName(ConstantsString.APP_NAME + " : " + requestBuilder.getLedName())
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
                        val serverRequest: NewServerRequest = requestBuilder.withRedValue(redValue)
                            .withGreenValue(greenValue).withBlueValue(blueValue).build()
                        coroutineScope.launch {
                            isLoaderVisible.value = true
                            val sentSuccessful = ledAppFacade.sendModeRequest(serverRequest)
                            isLoaderVisible.value = false
                            if (sentSuccessful) {
                                navController.navigate(
                                    Screen.LedScreen.route.replace(
                                        oldValue = "{ledIp}",
                                        newValue = serverRequest.ledIp
                                    ).replace(
                                        oldValue = "{ledName}",
                                        newValue = serverRequest.ledName
                                    )
                                )
                            } else {
                                dialogTextSendRequest.value = ConstantsString.SEND_ERROR_OCCURED
                                isRequestDialogVisible.value = true
                            }
                        }
                    },
                    buttonText = ConstantsString.SEND_REQUEST,
                    isVisible = isRequestDialogVisible,
                    dialogTitle = ConstantsString.DIALOG_TITLE_INFORMATION,
                    dialogText = dialogTextSendRequest.value,
                    isEnable = !isLoaderVisible.value
                )
            }
        }
    }
}


@Composable
fun CheckboxTileItem(tile: String, isSelected: Boolean, onSelectedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onSelectedChange(!isSelected) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onSelectedChange(it) },
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(
            text = tile,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
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
    var serverList by remember {
        mutableStateOf<List<Pair<String, String>>>(emptyList())
    }

    LaunchedEffect(key1 = true) {
        serverList = withContext(Dispatchers.IO) {
            ledAppFacade.getAllServersNameAndAddress()
        }
    }


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
    val isDeleteDialogVisible = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val isLoaderVisible = remember { mutableStateOf(false) }
    var dialogText = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    isLoaderVisible.value = true
                    if (!isDeleteDialogVisible.value) {
                        val connected = ledAppFacade.testConnectionWithServer(ledAddress)
                        isLoaderVisible.value = false
                        if (connected) {
                            navController.navigate(
                                Screen.LedScreen.route.replace(
                                    oldValue = "{ledIp}",
                                    newValue = ledAddress
                                ).replace(
                                    oldValue = "{ledName}",
                                    newValue = ledName
                                )
                            )
                        } else {
                            isDialogVisible.value = true
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .border(2.dp, MaterialTheme.colorScheme.background, shape = RectangleShape)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent(PointerEventPass.Initial)
                            event.changes.forEach { pointerInputChange ->
                                if (pointerInputChange.pressed) {
                                    val pressStartTime = System.currentTimeMillis()
                                    while (pointerInputChange.pressed) {
                                        if (System.currentTimeMillis() - pressStartTime > 800) {
                                            isDeleteDialogVisible.value = true
                                            break
                                        }
                                        awaitPointerEvent(PointerEventPass.Initial)
                                    }
                                }
                            }
                        }
                    }
                },
            colors = ButtonDefaults.buttonColors(Color.DarkGray)
        ) {
            Text(text = text, textAlign = TextAlign.Left)
            if (isDialogVisible.value) {
                AlertDialog(
                    onDismissRequest = { isDialogVisible.value = false },
                    confirmButton = {
                        Button(onClick = { isDialogVisible.value = false }) {
                            Text("OK")
                        }
                    },
                    text = { Text(ConstantsString.DIALOG_INFORMATION_LED_NOT_EXIST) },
                    title = { Text(ConstantsString.DIALOG_TITLE_INFORMATION) }
                )
            }
            if (isDeleteDialogVisible.value) {
                AlertDialog(
                    onDismissRequest = { isDeleteDialogVisible.value = false },
                    confirmButton = {
                        Button(onClick = {
                            coroutineScope.launch {
                                isDeleteDialogVisible.value = false
                                isLoaderVisible.value = true
                                val isDeleted = withContext(Dispatchers.IO) {
                                    ledAppFacade.deleteLedByName(ledName)
                                }
                                isLoaderVisible.value = false
                                if (isDeleted) {
                                    dialogText.value = ConstantsString.LED_DELETED
                                    isDialogVisible.value = true
                                    navController.navigate(Screen.MainScreen.route)

                                } else {
                                    dialogText.value = ConstantsString.ERROR_OCCURED
                                    isDialogVisible.value = true
                                }
                            }

                        }) {
                            Text(ConstantsString.YES)
                        }
                    },
                    dismissButton = {
                        Button(onClick = { isDeleteDialogVisible.value = false }) {
                            Text(ConstantsString.NO)
                        }
                    },
                    text = { Text(ConstantsString.DELETE_LED) },
                    title = { Text(ConstantsString.DIALOG_TITLE_INFORMATION) }
                )
            }
            if (isLoaderVisible.value) {
                CircularProgressIndicator()
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


@Composable
fun CircularProgressBar() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp).height(64.dp),
            color = MaterialTheme.colorScheme.secondary
        )
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

