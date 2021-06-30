package hu.hunyadym.canvas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp


class SnakeObject(coordinate: Coordinate, private val mapSize: Size) {
    companion object {
        private const val SNAKE_SIZE = 30
    }

    var coordinate by mutableStateOf(coordinate)
    fun update(direction: Direction) {
        coordinate = when (direction) {
            Direction.RIGHT -> coordinate.copy(x = coordinate.x + SNAKE_SIZE, y = coordinate.y)
            Direction.LEFT -> coordinate.copy(x = coordinate.x - SNAKE_SIZE, y = coordinate.y)
            Direction.UP -> coordinate.copy(x = coordinate.x, y = coordinate.y - SNAKE_SIZE)
            Direction.DOWN -> coordinate.copy(x = coordinate.x, y = coordinate.y + SNAKE_SIZE)
        }
        if (coordinate.x > mapSize.width - SNAKE_SIZE) {
            coordinate = coordinate.copy(x = 0)
        } else if (coordinate.x < -SNAKE_SIZE) {
            coordinate = coordinate.copy(x = mapSize.width.dp.value.toInt() - SNAKE_SIZE)
        } else if (coordinate.y < -SNAKE_SIZE) {
            coordinate = coordinate.copy(y = mapSize.height.dp.value.toInt() - SNAKE_SIZE)
        } else if (coordinate.y > mapSize.height - SNAKE_SIZE) {
            coordinate = coordinate.copy(y = 0)
        }
    }

    fun update(coordinate: Coordinate) {
        this.coordinate = coordinate
    }
}