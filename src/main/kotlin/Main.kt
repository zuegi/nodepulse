import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay
import model.DistanceRule
import model.NetworkGraph
import ui.NetworkView

@Composable
@Preview
fun App() {
    val graph = remember { NetworkGraph(DistanceRule(120f)) }
    LaunchedEffect(Unit) {
        graph.addRandomPoints(100)
        // Startpunkt initialisieren
        graph.points.firstOrNull()?.hasInformation = true
        while (true) {
            delay(1000)
            graph.spreadInformation()
        }
    }

    Box(Modifier.fillMaxSize()) {
        NetworkView(graph)
    }
}

fun main() =
    application {
        Window(onCloseRequest = ::exitApplication, title = "NodePulse") {
            App()
        }
    }
