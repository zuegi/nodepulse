package ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import model.NetworkGraph
import model.Point3D
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NetworkView(graph: NetworkGraph) {
    var rotationX by remember { mutableStateOf(20f) }
    var rotationY by remember { mutableStateOf(30f) }
    var scale by remember { mutableStateOf(1f) }

    // Canvas, in dem das 3D-Netzwerk gezeichnet wird
    println("Anzahl Punkte im Graph: ${graph.points.size}")
    Canvas(
        modifier =
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        // Rotation mit Drag (Maus oder Touch)
                        rotationY += pan.x * 0.5f
                        rotationX -= pan.y * 0.5f

                        // Zoom mit Touchpad oder Mausrad
                        if (zoom != 1.0f) {
                            scale *= zoom * 1.5f
                            scale = scale.coerceIn(0.1f, 5f) // Begrenze den Zoom
                        }
                    }
                },
    ) {
        // Berechne die Mitte des Canvas
        val centerX = size.width / 2
        val centerY = size.height / 2

        // Debugging: Stelle sicher, dass der Canvas groß genug ist und die Mitte stimmt
        println("Canvas Größe: ${size.width}x${size.height}, Mitte: $centerX, $centerY")

        // Zeichne alle Punkte im Netzwerk
        for (point in graph.points) {
            // Berechne die Rotation für den aktuellen Punkt
            val rotated = rotate(point, rotationX, rotationY)

            // Skalierung anwenden
            val projectedX = rotated.x * scale
            val projectedY = rotated.y * scale - rotated.z * 0.5f * scale

            // Berechne die Position des Punktes im Canvas (die Mitte berücksichtigen)
            val px = centerX + projectedX
            val py = centerY + projectedY

            // Debugging: Ausgabe der Position jedes Punktes
            println("Punkt $point -> Position: ($px, $py), Skalierung: $scale")

            // Zeichne die Verbindungen (Nachbarn)
            point.neighbors.forEach { neighbor ->
                val rotatedNeighbor = rotate(neighbor, rotationX, rotationY)
                val nx = centerX + rotatedNeighbor.x * scale
                val ny = centerY + rotatedNeighbor.y * scale - rotatedNeighbor.z * 0.5f * scale

                drawLine(
                    color = Color.Gray,
                    start = Offset(px, py),
                    end = Offset(nx, ny),
                    strokeWidth = 1f,
                )
            }

            // Zeichne den Punkt (rot für aktive, blau für nicht aktive)
            drawCircle(
                color = if (point.hasInformation) Color.Red else Color.Blue,
                center = Offset(px, py),
                radius = 5f,
            )
        }
    }
}

/**
 * Rotiert einen Punkt im 3D-Raum um die X- und Y-Achse.
 */
fun rotate(
    point: Point3D,
    angleX: Float,
    angleY: Float,
): Point3D {
    val radX = Math.toRadians(angleX.toDouble())
    val radY = Math.toRadians(angleY.toDouble())

    // Rotation um die Y-Achse (horizontal drehen)
    val cosY = cos(radY)
    val sinY = sin(radY)
    var x = point.x * cosY - point.z * sinY
    var z = point.x * sinY + point.z * cosY

    // Rotation um die X-Achse (vertikal kippen)
    val cosX = cos(radX)
    val sinX = sin(radX)
    val y = point.y * cosX - z * sinX
    z = point.y * sinX + z * cosX

    return Point3D(point.id, x.toFloat(), y.toFloat(), z.toFloat()) // Benutze die berechneten Werte
}
