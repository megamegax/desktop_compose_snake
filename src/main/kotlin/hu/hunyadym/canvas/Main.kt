package hu.hunyadym.canvas

import androidx.compose.desktop.Window
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize

@OptIn(ExperimentalComposeUiApi::class)
fun main() =
    Window(title = "Snake", size = IntSize(900, 900)) {
        canvas(Size(900F, 900F))
    }