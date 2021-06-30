package hu.hunyadym.canvas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.random.Random


class FruitObject(position: Coordinate, color: Color) {

    companion object {
        fun new(): FruitObject {
          return  FruitObject(getNextCoordinate(), getNextColor())
        }

        private fun getNextColor(): Color {
            val color = Color(Random.nextInt())
            return if(color == BackgroundColor) getNextColor() else color
        }


        private fun getNextCoordinate(): Coordinate {
            return Coordinate(generateCoordinate(), generateCoordinate())
        }

        private fun generateCoordinate(): Int {
            val coordinate = Random.nextInt(0, 900 - 30)
            return if (coordinate % 30 == 0) coordinate else generateCoordinate()
        }
    }

    val size: Size = Size(30f, 30f)
    var color: Color by mutableStateOf(color)
    var position by mutableStateOf(position)
}