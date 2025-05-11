package model

import kotlin.math.sqrt

class DistanceRule(
    private val maxDistance: Float,
) : ConnectionRule {
    /**
     * Bestimmt, ob zwei Punkte im 3D-Raum miteinander verbunden werden dürfen.
     *
     * Die Verbindung ist erlaubt, wenn die euklidische Distanz zwischen den Punkten
     * kleiner als ein definierter Maximalwert ist.
     *
     * Diese Regel bildet z.B. ab, dass nur nahe beieinanderliegende Knoten kommunizieren können –
     * vergleichbar mit Reichweitenbeschränkungen in Funknetzwerken oder biologischen Systemen.
     *
     * @param a Erster Punkt
     * @param b Zweiter Punkt
     * @return true, wenn die Punkte verbunden werden dürfen
     */
    override fun canConnect(
        a: Point3D,
        b: Point3D,
    ): Boolean {
        val dx = a.x - b.x
        val dy = a.y - b.y
        val dz = a.z - b.z
        val distance = sqrt(dx * dx + dy * dy + dz * dz)
        return distance < maxDistance
    }
}
