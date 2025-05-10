package ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import model.NetworkGraph

@Composable
fun NetworkView(graph: NetworkGraph) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        graph.points.forEach { point ->
            val px = point.x
            val py = point.y - point.z * 0.5f

            point.neighbors.forEach { neighbor ->
                val nx = neighbor.x
                val ny = neighbor.y - neighbor.z * 0.5f
                drawLine(
                    color = Color.Gray,
                    start = Offset(px, py),
                    end = Offset(nx, ny),
                    strokeWidth = 1f,
                )
            }

            drawCircle(
                color = if (point.hasInformation) Color.Red else Color.Blue,
                center = Offset(px, py),
                radius = 5f,
            )
        }
    }
}
