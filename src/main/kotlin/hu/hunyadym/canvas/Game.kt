package hu.hunyadym.canvas

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.key.Key
import java.lang.RuntimeException

enum class Direction {
    UP, LEFT, RIGHT, DOWN;

    fun isOpposite(otherDirection: Direction): Boolean {
        return (this == Direction.UP && otherDirection == Direction.DOWN) || (this == Direction.DOWN && otherDirection == Direction.UP) ||
                (this == Direction.LEFT && otherDirection == Direction.RIGHT) || (this == Direction.RIGHT && otherDirection == Direction.LEFT)

    }
}


class Game(val mapSize: Size) {
    var direction by mutableStateOf(Direction.LEFT)
    var snake = mutableStateListOf<SnakeObject>()
        private set
    var elapsed by mutableStateOf(0L)
    var score by mutableStateOf(0)
    var fruit by mutableStateOf(
        FruitObject(
            Coordinate(210, 210),
            Color(255, 0, 0, 255)
        )
    )
    private var previousTimeNanos: Long = Long.MAX_VALUE
    private var startTime = 0L

    init {
        start(mapSize)
    }

    fun start(mapSize: Size) {
        previousTimeNanos = System.nanoTime()
        startTime = previousTimeNanos

        snake.clear()
        snake.add(SnakeObject(Coordinate(0, 210), mapSize))
        snake.add(SnakeObject(Coordinate(30, 210), mapSize))
        snake.add(SnakeObject(Coordinate(60, 210), mapSize))
        println()
    }

    var accumulator = 0.0
    fun update(nanos: Long) {
        val dt = (nanos - previousTimeNanos).coerceAtLeast(0)
        accumulator += dt
        previousTimeNanos = nanos
        elapsed = nanos - startTime
        while (accumulator > 1.0 / 0.01) {
            for (i in snake.size - 1 downTo 1) {
                snake[i].update(snake[i - 1].coordinate)
            }
            snake[0].update(direction)
            if (isHappyCollision()) {
                fruit = FruitObject.new()
                snake.add(SnakeObject(Coordinate(snake.last().coordinate.x + 30, snake.last().coordinate.y), mapSize))
                score++
            }
            if (isSadCollision()) {
                throw RuntimeException("PANIC")
            }
            accumulator -= 1.0 / 0.01
        }
    }

    private fun isSadCollision(): Boolean {
        return snake.subList(1, snake.size).any {
            it.coordinate == snake.first().coordinate
        }
    }


    private fun isHappyCollision(): Boolean {
        return snake.first().coordinate == fruit.position
    }
}

@Composable
fun canvas(size: Size) {
    val game = remember { Game(size) }
    val paint = Paint().asFrameworkPaint()
    LocalAppWindow.current.keyboard.setShortcut(Key.W) {
        game.direction = Direction.UP
    }
    LocalAppWindow.current.keyboard.setShortcut(Key.A) {
        game.direction = Direction.LEFT
    }
    LocalAppWindow.current.keyboard.setShortcut(Key.S) {
        game.direction = Direction.DOWN
    }
    LocalAppWindow.current.keyboard.setShortcut(Key.D) {
        game.direction = Direction.RIGHT
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        paint.apply {
            isAntiAlias = true
        }
        drawIntoCanvas {
            drawRect(BackgroundColor, size = size)
            game.fruit.let {
                drawRoundRect(it.color, Offset(it.position.x.toFloat(), it.position.y.toFloat()), it.size)
            }
            game.snake.forEach { coordinates ->
                drawRect(
                    Color(0, 0, 0, 255),
                    Offset(coordinates.coordinate.x.toFloat(), coordinates.coordinate.y.toFloat()),
                    Size(30F, 30F)
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis {
                game.update(it)
            }
        }
    }
}