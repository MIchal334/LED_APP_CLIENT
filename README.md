# LED Control Client for Android

## Overview

This project is an Android client application for controlling WS2812B LED strips via an ESP8266 server. The app is developed in Kotlin and uses Jetpack Compose for the user interface. It communicates with the LED control server using a REST API. The application allows users to add LED devices via their API address, select LEDs from a list, synchronize with individual LED devices, and choose colors, transition modes, or lighting modes. The app follows a hexagonal architecture pattern to ensure clean separation of concerns and ease of extension.

## Features

- **Add LED Devices**: Add and manage LED devices by entering their API addresses.
- **Device List Management**: Select and manage LEDs from a list of connected devices.
- **Color and Mode Selection**: Set colors, transition effects, and lighting modes for the selected LED device.
- **Synchronization**: Sync settings with individual LED devices.

## Technologies Used

- **Programming Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: Hexagonal Architecture
- **Communication Protocol**: REST API

