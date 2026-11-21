package com.example.pockettrackkaiju_tea.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

object TransicionesNavegacion {
    private const val DURACION_MS = 300


    val entrar: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Start,
            tween(DURACION_MS)
        ) + fadeIn(tween(DURACION_MS))
    }
    val salir: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(tween(DURACION_MS))
    }

    val volverEntrar: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(tween(DURACION_MS))
    }


    val volverSalir: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.End,
            tween(DURACION_MS)
        ) + fadeOut(tween(DURACION_MS))
    }
}