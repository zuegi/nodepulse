package model

interface ConnectionRule {
    fun canConnect(
        a: Point3D,
        b: Point3D,
    ): Boolean
}
