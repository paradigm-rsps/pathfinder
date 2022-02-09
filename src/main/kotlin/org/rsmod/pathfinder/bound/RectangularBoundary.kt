package org.rsmod.pathfinder.bound

internal fun reachRectangle(
    flags: Array<IntArray?>,
    defaultFlag: Int,
    baseX: Int,
    baseY: Int,
    z: Int,
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
                defaultFlag,
                baseX,
                baseY,
                z,
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
        defaultFlag,
        baseX,
        baseY,
        z,
        accessBitMask,
        localSrcX,
        localSrcY,
        localDestX,
        localDestY,
        destWidth,
        destHeight
    )
}
