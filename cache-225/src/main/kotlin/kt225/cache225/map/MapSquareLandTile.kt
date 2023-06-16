package kt225.cache225.map

/**
 * @author Jordan Abraham
 */
data class MapSquareLandTile(
    val height: MapSquareLandTileHeight,
    val overlay: MapSquareLandTileOverlay,
    val collision: MapSquareLandTileCollision,
    val underlay: MapSquareLandTileUnderlay
)
