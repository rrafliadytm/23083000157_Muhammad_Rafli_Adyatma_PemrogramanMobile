package com.example.week12_mynoteapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.example.week12_mynoteapp.R

/**
 * Tema kustom "MyNote" dengan nuansa Sticky Note (kuning memo).
 */
@Composable
fun MyNoteTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
        primary = colorResource(id = R.color.note_primary),
        onPrimary = colorResource(id = R.color.note_on_primary),
        surface = colorResource(id = R.color.note_surface),
        onSurface = colorResource(id = R.color.note_on_surface),
        background = colorResource(id = R.color.note_background),
        secondaryContainer = colorResource(id = R.color.note_secondary_container),
        onSecondaryContainer = colorResource(id = R.color.note_on_secondary_container)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
