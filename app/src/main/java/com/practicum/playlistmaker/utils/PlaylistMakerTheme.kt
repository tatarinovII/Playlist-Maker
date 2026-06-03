package com.practicum.playlistmaker.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.practicum.playlistmaker.R

@Composable
fun PlaylistMakerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = colorResource(R.color.white),
            secondary = colorResource(R.color.black),
            background = colorResource(R.color.black),
            surface = colorResource(R.color.white),
            primaryContainer = colorResource(R.color.white)
        )
    } else {
        lightColorScheme(
            primary = colorResource(R.color.black),
            secondary = colorResource(R.color.light_grey),
            background = colorResource(R.color.white),
            surface = colorResource(R.color.double_light_grey),
            primaryContainer = colorResource(R.color.light_grey)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}