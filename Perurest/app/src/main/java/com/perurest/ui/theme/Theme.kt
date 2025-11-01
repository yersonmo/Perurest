package com.perurest.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val Colors = lightColorScheme()


@Composable
fun PerurestTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Colors, typography = androidx.compose.material3.Typography(), content = content)
}