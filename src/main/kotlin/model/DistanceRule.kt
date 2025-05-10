package model

import kotlin.math.sqrt

class DistanceRule(
    private val maxDistance: Float,
) : ConnectionRule {
    override fun canConnect(
        a: Point3D,
        b: Point3D,
    ): Boolean {
        val dx = a.x - b.x
        val dy = a.y - b.y
        val dz = a.z - b.z
        return sqrt(dx * dx + dy * dy + dz * dz) < maxDistance
    }
}
