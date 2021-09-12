package org.rsmod.pathfinder.bound

internal fun reachRectangle(
    flags: IntArray,
    mapSize: Int,
    accessBitMask: Int,
    localSrcX: Int,
    localSrcY: Int,
    localDestX: Int,
    localDestY: Int,
    srcSize: Int,
    destWidth: Int,
    destHeight: Int
): Boolean = when {
    srcSize > 1 -> {
        RectangleBoundaryUtils
            .collides(localSrcX, localSrcY, localDestX, localDestY, srcSize, srcSize, destWidth, destHeight) ||
            RectangleBoundaryUtils.reachRectangleN(
                flags,
                mapSize,
                accessBitMask,
                localSrcX,
                localSrcY,
                localDestX,
                localDestY,
                srcSize,
                srcSize,
                destWidth,
                destHeight
            )
    }
    else -> RectangleBoundaryUtils.reachRectangle1(
        flags,
        mapSize,
        accessBitMask,
        localSrcX,
        localSrcY,
        localDestX,
        localDestY,
        destWidth,
        destHeight
    )
}
