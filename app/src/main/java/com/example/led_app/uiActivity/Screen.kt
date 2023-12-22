package com.example.led_app.uiActivity

sealed class Screen(val route: String){
    object MainScreen: Screen("main_screen")
    object AddNewLedScreen: Screen("add_led_screen")
    object LedScreen: Screen("led_screen")
}