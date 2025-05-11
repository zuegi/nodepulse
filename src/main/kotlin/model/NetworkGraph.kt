package model

class NetworkGraph(
    private val rule: ConnectionRule,
) {
    val points = mutableListOf<Point3D>()

    fun addRandomPoints(count: Int) {
        repeat(count) { id ->
            val x = (0..800).random().toFloat()
            val y = (0..600).random().toFloat()
            val z = (0..300).random().toFloat()
            points.add(Point3D(id, x, y, z))
        }
        updateConnections()
    }

    fun updateConnections() {
        // Schritt 1: Verbindung gemäß Regel + Mindestanzahl
        for (a in points) {
            a.neighbors.clear()

            val connectables = points.filter { it != a && rule.canConnect(a, it) }.toMutableList()
            a.neighbors.addAll(connectables)

            // Mindestanzahl an Verbindungen (z. B. 3)
            if (a.neighbors.size < 3) {
                val additional =
                    points
                        .filter { it != a && !a.neighbors.contains(it) }
                        .sortedBy { b ->
                            val dx = a.x - b.x
                            val dy = a.y - b.y
                            val dz = a.z - b.z
                            dx * dx + dy * dy + dz * dz
                        }.take(3 - a.neighbors.size)
                a.neighbors.addAll(additional)
            }
        }

        // Schritt 2: Komponenten prüfen und zusammenfügen
        val components = findConnectedComponents()
        if (components.size > 1) {
            for (i in 0 until components.size - 1) {
                val a = components[i].first()
                val b = components[i + 1].first()
                a.neighbors.add(b)
                b.neighbors.add(a)
            }
        }
    }

    fun spreadInformation() {
        val newWave = mutableSetOf<Point3D>()
        for (p in points) {
            if (p.hasInformation) {
                p.neighbors.forEach {
                    if (!it.hasInformation) {
                        newWave.add(it)
                    }
                }
            }
        }
        newWave.forEach { it.hasInformation = true }
    }

    /**
     * Diese Methode garantiert, dass das Netzwerk zusammenhängend ist:
     * - Erst werden Verbindungen gemäß Regel und Mindestanzahl aufgebaut.
     * - Danach wird geprüft, ob alle Punkte Teil einer einzigen verbundenen Komponente sind.
     * - Falls nicht, werden zusätzliche Verbindungen zwischen den Komponenten hergestellt.
     */
    private fun findConnectedComponents(): List<Set<Point3D>> {
        val visited = mutableSetOf<Point3D>()
        val components = mutableListOf<Set<Point3D>>()

        for (start in points) {
            if (start !in visited) {
                val component = mutableSetOf<Point3D>()
                val queue = ArrayDeque<Point3D>()
                queue.add(start)
                visited.add(start)

                while (queue.isNotEmpty()) {
                    val current = queue.removeFirst()
                    component.add(current)
                    for (neighbor in current.neighbors) {
                        if (neighbor !in visited) {
                            visited.add(neighbor)
                            queue.add(neighbor)
                        }
                    }
                }

                components.add(component)
            }
        }

        return components
    }
}
