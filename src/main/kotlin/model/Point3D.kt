package model

data class Point3D(
    val id: Int,
    var x: Float,
    var y: Float,
    var z: Float,
) {
    val neighbors = mutableSetOf<Point3D>()
    var hasInformation = false
}
