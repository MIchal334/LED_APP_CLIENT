package com.example.led_app.uiActivity

sealed class Screen(val route: String){
    object MainScreen: Screen("main_screen")
    object AddNewLedScreen: Screen("add_led_screen")
    object LedScreen: Screen("led_screen/{ledIp}/{ledName}")
    object ColorScreen: Screen("color_screen/{requestBuilder}")
    object ChangeModeScreen: Screen("changeModeScreen/{requestBuilder}")

    object LedModeScreen: Screen("ledModeScreen/{requestBuilder}")

}