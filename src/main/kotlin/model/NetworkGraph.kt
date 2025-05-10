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
        for (a in points) {
            a.neighbors.clear()
            for (b in points) {
                if (a != b && rule.canConnect(a, b)) {
                    a.neighbors.add(b)
                }
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
}
