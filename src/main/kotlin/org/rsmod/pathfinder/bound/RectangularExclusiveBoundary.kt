package org.rsmod.pathfinder.bound

/**
 * @author Kris | 12/09/2021
 */
internal fun reachExclusiveRectangle(
    flags: Array<IntArray?>,
    defaultFlag: Int,
    x: Int,
    y: Int,
    z: Int,
    accessBitMask: Int,
    destX: Int,
    destY: Int,
    srcSize: Int,
    destWidth: Int,
    destHeight: Int
): Boolean = when {
    srcSize > 1 -> {
        if (RectangleBoundaryUtils.collides(x, y, destX, destY, srcSize, srcSize, destWidth, destHeight)) false
        else RectangleBoundaryUtils.reachRectangleN(
            flags,
            defaultFlag,
            x,
            y,
            z,
            accessBitMask,
            destX,
            destY,
            srcSize,
            srcSize,
            destWidth,
            destHeight
        )
    }
    else -> {
        if (RectangleBoundaryUtils.collides(x, y, destX, destY, srcSize, srcSize, destWidth, destHeight)) false
        else RectangleBoundaryUtils.reachRectangle1(
            flags,
            defaultFlag,
            x,
            y,
            z,
            accessBitMask,
            destX,
            destY,
            destWidth,
            destHeight
        )
    }
}
