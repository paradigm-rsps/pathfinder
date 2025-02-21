@file:Suppress("DuplicatedCode")

package org.rsmod.pathfinder.bound

import org.rsmod.pathfinder.flag.CollisionFlag

internal fun reachWallDeco(
    flags: Array<IntArray?>,
    defaultFlag: Int,
    x: Int,
    y: Int,
    z: Int,
    destX: Int,
    destY: Int,
    srcSize: Int,
    shape: Int,
    rot: Int
): Boolean = when {
    srcSize == 1 && x == destX && destY == y -> true
    srcSize != 1 && destX >= x && srcSize + x + -1 >= destX &&
        srcSize + destY + -1 >= destY -> true
    srcSize == 1 -> reachWallDeco1(
        flags,
        defaultFlag,
        x,
        y,
        z,
        destX,
        destY,
        shape,
        rot
    )
    else -> reachWallDecoN(
        flags,
        defaultFlag,
        x,
        y,
        z,
        destX,
        destY,
        srcSize,
        shape,
        rot
    )
}

private fun reachWallDeco1(
    flags: Array<IntArray?>,
    defaultFlag: Int,
    x: Int,
    y: Int,
    z: Int,
    destX: Int,
    destY: Int,
    shape: Int,
    rot: Int
): Boolean {
    if (shape in 6..7) {
        when (rot.alteredRotation(shape)) {
            0 -> {
                if (x == destX + 1 && y == destY &&
                    (flags[defaultFlag, x, y, z] and CollisionFlag.WALL_WEST) == 0
                ) return true
                if (x == destX && y == destY - 1 &&
                    (flags[defaultFlag, x, y, z] and CollisionFlag.WALL_NORTH) == 0
                ) return true
            }
            1 -> {
                if (x == destX - 1 && y == destY &&
                    (flags[defaultFlag, x, y, z] and CollisionFlag.WALL_EAST) == 0
                ) return true
                if (x == destX && y == destY - 1 &&
                    (flags[defaultFlag, x, y, z] and CollisionFlag.WALL_NORTH) == 0
                ) return true
            }
            2 -> {
                if (x == destX - 1 && y == destY &&
                    (flags[defaultFlag, x, y, z] and CollisionFlag.WALL_EAST) == 0
                ) return true
                if (x == destX && y == destY + 1 &&
                    (flags[defaultFlag, x, y, z] and CollisionFlag.WALL_SOUTH) == 0
                ) return true
            }
            3 -> {
                if (x == destX + 1 && y == destY &&
                    (flags[defaultFlag, x, y, z] and CollisionFlag.WALL_WEST) == 0
                ) return true
                if (x == destX && y == destY + 1 &&
                    (flags[defaultFlag, x, y, z] and CollisionFlag.WALL_SOUTH) == 0
                ) return true
            }
        }
    } else if (shape == 8) {
        if (x == destX && y == destY + 1 &&
            (flags[defaultFlag, x, y, z] and CollisionFlag.WALL_SOUTH) == 0
        ) return true
        if (x == destX && y == destY - 1 &&
            (flags[defaultFlag, x, y, z] and CollisionFlag.WALL_NORTH) == 0
        ) return true
        if (x == destX - 1 && y == destY &&
            (flags[defaultFlag, x, y, z] and CollisionFlag.WALL_EAST) == 0
        ) return true

        return x == destX + 1 && y == destY &&
            (flags[defaultFlag, x, y, z] and CollisionFlag.WALL_WEST) == 0
    }
    return false
}

private fun reachWallDecoN(
    flags: Array<IntArray?>,
    defaultFlag: Int,
    x: Int,
    y: Int,
    z: Int,
    destX: Int,
    destY: Int,
    srcSize: Int,
    shape: Int,
    rot: Int
): Boolean {
    val east = x + srcSize - 1
    val north = y + srcSize - 1
    if (shape in 6..7) {
        when (rot.alteredRotation(shape)) {
            0 -> {
                if (x == destX + 1 && y <= destY && north >= destY &&
                    (flags[defaultFlag, x, destY, z] and CollisionFlag.WALL_WEST) == 0
                ) return true
                if (x <= destX && y == destY - srcSize && east >= destX &&
                    (flags[defaultFlag, destX, north, z] and CollisionFlag.WALL_NORTH) == 0
                ) return true
            }
            1 -> {
                if (x == destX - srcSize && y <= destY && north >= destY &&
                    (flags[defaultFlag, east, destY, z] and CollisionFlag.WALL_EAST) == 0
                ) return true
                if (x <= destX && y == destY - srcSize && east >= destX &&
                    (flags[defaultFlag, destX, north, z] and CollisionFlag.WALL_NORTH) == 0
                ) return true
            }
            2 -> {
                if (x == destX - srcSize && y <= destY && north >= destY &&
                    (flags[defaultFlag, east, destY, z] and CollisionFlag.WALL_EAST) == 0
                ) return true
                if (x <= destX && y == destY + 1 && east >= destX &&
                    (flags[defaultFlag, destX, y, z] and CollisionFlag.WALL_SOUTH) == 0
                ) return true
            }
            3 -> {
                if (x == destX + 1 && y <= destY && north >= destY &&
                    (flags[defaultFlag, x, destY, z] and CollisionFlag.WALL_WEST) == 0
                ) return true
                if (x <= destX && y == destY + 1 && east >= destX &&
                    (flags[defaultFlag, destX, y, z] and CollisionFlag.WALL_SOUTH) == 0
                ) return true
            }
        }
    } else if (shape == 8) {
        if (x <= destX && y == destY + 1 && east >= destX &&
            (flags[defaultFlag, destX, y, z] and CollisionFlag.WALL_SOUTH) == 0
        ) return true
        if (x <= destX && y == destY - srcSize && east >= destX &&
            (flags[defaultFlag, destX, north, z] and CollisionFlag.WALL_NORTH) == 0
        ) return true
        if (x == destX - srcSize && y <= destY && north >= destY &&
            (flags[defaultFlag, east, destY, z] and CollisionFlag.WALL_EAST) == 0
        ) return true

        return x == destX + 1 && y <= destY && north >= destY &&
            (flags[defaultFlag, x, destY, z] and CollisionFlag.WALL_WEST) == 0
    }
    return false
}

private fun Int.alteredRotation(shape: Int): Int {
    return if (shape == 7) (this + 2) and 0x3 else this
}

@Suppress("NOTHING_TO_INLINE")
private inline operator fun Array<IntArray?>.get(
    defaultFlag: Int,
    x: Int,
    y: Int,
    z: Int
): Int {
    val zone = this[getZoneIndex(x, y, z)] ?: return defaultFlag
    return zone[getIndexInZone(x, y)]
}

@Suppress("NOTHING_TO_INLINE")
private inline fun getZoneIndex(x: Int, y: Int, z: Int): Int {
    return ((x shr 3) and 0x7FF) or (((y shr 3) and 0x7FF) shl 11) or ((z and 0x3) shl 22)
}

@Suppress("NOTHING_TO_INLINE")
private inline fun getIndexInZone(x: Int, y: Int): Int {
    return (x and 0x7) or ((y and 0x7) shl 3)
}
