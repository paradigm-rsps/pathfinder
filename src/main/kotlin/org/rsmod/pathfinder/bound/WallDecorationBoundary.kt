@file:Suppress("DuplicatedCode")

package org.rsmod.pathfinder.bound

import org.rsmod.pathfinder.flag.CollisionFlag

internal fun reachWallDeco(
    flags: Array<IntArray?>,
    defaultFlag: Int,
    baseX: Int,
    baseY: Int,
    z: Int,
    localSrcX: Int,
    localSrcY: Int,
    localDestX: Int,
    localDestY: Int,
    srcSize: Int,
    shape: Int,
    rot: Int
): Boolean = when {
    srcSize == 1 && localSrcX == localDestX && localDestY == localSrcY -> true
    srcSize != 1 && localDestX >= localSrcX && srcSize + localSrcX + -1 >= localDestX &&
        srcSize + localDestY + -1 >= localDestY -> true
    srcSize == 1 -> reachWallDeco1(
        flags,
        defaultFlag,
        baseX,
        baseY,
        z,
        localSrcX,
        localSrcY,
        localDestX,
        localDestY,
        shape,
        rot
    )
    else -> reachWallDecoN(
        flags,
        defaultFlag,
        baseX,
        baseY,
        z,
        localSrcX,
        localSrcY,
        localDestX,
        localDestY,
        srcSize,
        shape,
        rot
    )
}

private fun reachWallDeco1(
    flags: Array<IntArray?>,
    defaultFlag: Int,
    baseX: Int,
    baseY: Int,
    z: Int,
    localSrcX: Int,
    localSrcY: Int,
    localDestX: Int,
    localDestY: Int,
    shape: Int,
    rot: Int
): Boolean {
    if (shape in 6..7) {
        when (rot.alteredRotation(shape)) {
            0 -> {
                if (localSrcX == localDestX + 1 && localSrcY == localDestY &&
                    (flags[defaultFlag, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_WEST) == 0
                ) return true
                if (localSrcX == localDestX && localSrcY == localDestY - 1 &&
                    (flags[defaultFlag, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_NORTH) == 0
                ) return true
            }
            1 -> {
                if (localSrcX == localDestX - 1 && localSrcY == localDestY &&
                    (flags[defaultFlag, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_EAST) == 0
                ) return true
                if (localSrcX == localDestX && localSrcY == localDestY - 1 &&
                    (flags[defaultFlag, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_NORTH) == 0
                ) return true
            }
            2 -> {
                if (localSrcX == localDestX - 1 && localSrcY == localDestY &&
                    (flags[defaultFlag, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_EAST) == 0
                ) return true
                if (localSrcX == localDestX && localSrcY == localDestY + 1 &&
                    (flags[defaultFlag, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_SOUTH) == 0
                ) return true
            }
            3 -> {
                if (localSrcX == localDestX + 1 && localSrcY == localDestY &&
                    (flags[defaultFlag, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_WEST) == 0
                ) return true
                if (localSrcX == localDestX && localSrcY == localDestY + 1 &&
                    (flags[defaultFlag, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_SOUTH) == 0
                ) return true
            }
        }
    } else if (shape == 8) {
        if (localSrcX == localDestX && localSrcY == localDestY + 1 &&
            (flags[defaultFlag, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_SOUTH) == 0
        ) return true
        if (localSrcX == localDestX && localSrcY == localDestY - 1 &&
            (flags[defaultFlag, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_NORTH) == 0
        ) return true
        if (localSrcX == localDestX - 1 && localSrcY == localDestY &&
            (flags[defaultFlag, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_EAST) == 0
        ) return true

        return localSrcX == localDestX + 1 && localSrcY == localDestY &&
            (flags[defaultFlag, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_WEST) == 0
    }
    return false
}

private fun reachWallDecoN(
    flags: Array<IntArray?>,
    defaultFlag: Int,
    baseX: Int,
    baseY: Int,
    z: Int,
    localSrcX: Int,
    localSrcY: Int,
    localDestX: Int,
    localDestY: Int,
    srcSize: Int,
    shape: Int,
    rot: Int
): Boolean {
    val east = localSrcX + srcSize - 1
    val north = localSrcY + srcSize - 1
    if (shape in 6..7) {
        when (rot.alteredRotation(shape)) {
            0 -> {
                if (localSrcX == localDestX + 1 && localSrcY <= localDestY && north >= localDestY &&
                    (flags[defaultFlag, baseX, baseY, localSrcX, localDestY, z] and CollisionFlag.WALL_WEST) == 0
                ) return true
                if (localSrcX <= localDestX && localSrcY == localDestY - srcSize && east >= localDestX &&
                    (flags[defaultFlag, baseX, baseY, localDestX, north, z] and CollisionFlag.WALL_NORTH) == 0
                ) return true
            }
            1 -> {
                if (localSrcX == localDestX - srcSize && localSrcY <= localDestY && north >= localDestY &&
                    (flags[defaultFlag, baseX, baseY, east, localDestY, z] and CollisionFlag.WALL_EAST) == 0
                ) return true
                if (localSrcX <= localDestX && localSrcY == localDestY - srcSize && east >= localDestX &&
                    (flags[defaultFlag, baseX, baseY, localDestX, north, z] and CollisionFlag.WALL_NORTH) == 0
                ) return true
            }
            2 -> {
                if (localSrcX == localDestX - srcSize && localSrcY <= localDestY && north >= localDestY &&
                    (flags[defaultFlag, baseX, baseY, east, localDestY, z] and CollisionFlag.WALL_EAST) == 0
                ) return true
                if (localSrcX <= localDestX && localSrcY == localDestY + 1 && east >= localDestX &&
                    (flags[defaultFlag, baseX, baseY, localDestX, localSrcY, z] and CollisionFlag.WALL_SOUTH) == 0
                ) return true
            }
            3 -> {
                if (localSrcX == localDestX + 1 && localSrcY <= localDestY && north >= localDestY &&
                    (flags[defaultFlag, baseX, baseY, localSrcX, localDestY, z] and CollisionFlag.WALL_WEST) == 0
                ) return true
                if (localSrcX <= localDestX && localSrcY == localDestY + 1 && east >= localDestX &&
                    (flags[defaultFlag, baseX, baseY, localDestX, localSrcY, z] and CollisionFlag.WALL_SOUTH) == 0
                ) return true
            }
        }
    } else if (shape == 8) {
        if (localSrcX <= localDestX && localSrcY == localDestY + 1 && east >= localDestX &&
            (flags[defaultFlag, baseX, baseY, localDestX, localSrcY, z] and CollisionFlag.WALL_SOUTH) == 0
        ) return true
        if (localSrcX <= localDestX && localSrcY == localDestY - srcSize && east >= localDestX &&
            (flags[defaultFlag, baseX, baseY, localDestX, north, z] and CollisionFlag.WALL_NORTH) == 0
        ) return true
        if (localSrcX == localDestX - srcSize && localSrcY <= localDestY && north >= localDestY &&
            (flags[defaultFlag, baseX, baseY, east, localDestY, z] and CollisionFlag.WALL_EAST) == 0
        ) return true

        return localSrcX == localDestX + 1 && localSrcY <= localDestY && north >= localDestY &&
            (flags[defaultFlag, baseX, baseY, localSrcX, localDestY, z] and CollisionFlag.WALL_WEST) == 0
    }
    return false
}

private fun Int.alteredRotation(shape: Int): Int {
    return if (shape == 7) (this + 2) and 0x3 else this
}

@Suppress("NOTHING_TO_INLINE")
private inline operator fun Array<IntArray?>.get(
    defaultFlag: Int,
    baseX: Int,
    baseY: Int,
    localX: Int,
    localY: Int,
    z: Int
): Int {
    val x = baseX + localX
    val y = baseY + localY
    val zone = this[getZoneIndex(x, y, z)] ?: return defaultFlag
    return zone[getIndexInZone(x, y)]
}

@Suppress("NOTHING_TO_INLINE")
private inline fun getZoneIndex(x: Int, y: Int, z: Int): Int {
    return (x and 0x7FF) or ((y and 0x7FF) shl 11) or ((z and 0x3) shl 22)
}

@Suppress("NOTHING_TO_INLINE")
private inline fun getIndexInZone(x: Int, y: Int): Int {
    return (x and 0x7) or ((y and 0x7) shl 3)
}
