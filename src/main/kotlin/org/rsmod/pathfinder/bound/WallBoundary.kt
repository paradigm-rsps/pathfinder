@file:Suppress("DuplicatedCode")

package org.rsmod.pathfinder.bound

import org.rsmod.pathfinder.flag.CollisionFlag

internal fun reachWall(
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
    srcSize == 1 && x == destX && y == destY -> true
    srcSize != 1 && destX >= x && srcSize + x - 1 >= destX &&
        srcSize + destY - 1 >= destY -> true
    srcSize == 1 -> reachWall1(
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
    else -> reachWallN(
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

private fun reachWall1(
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
    when (shape) {
        0 -> {
            when (rot) {
                0 -> {
                    if (x == destX - 1 && y == destY)
                        return true
                    if (x == destX && y == destY + 1 &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (x == destX && y == destY - 1 &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                1 -> {
                    if (x == destX && y == destY + 1)
                        return true
                    if (x == destX - 1 && y == destY &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (x == destX + 1 && y == destY &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                }
                2 -> {
                    if (x == destX + 1 && y == destY)
                        return true
                    if (x == destX && y == destY + 1 &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (x == destX && y == destY - 1 &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                3 -> {
                    if (x == destX && y == destY - 1)
                        return true
                    if (x == destX - 1 && y == destY &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (x == destX + 1 && y == destY &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                }
            }
        }
        2 -> {
            when (rot) {
                0 -> {
                    if (x == destX - 1 && y == destY)
                        return true
                    if (x == destX && y == destY + 1)
                        return true
                    if (x == destX + 1 && y == destY &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                    if (x == destX && y == destY - 1 &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                1 -> {
                    if (x == destX - 1 && y == destY &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (x == destX && y == destY + 1)
                        return true
                    if (x == destX + 1 && y == destY)
                        return true
                    if (x == destX && y == destY - 1 &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                2 -> {
                    if (x == destX - 1 && y == destY &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (x == destX && y == destY + 1 &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (x == destX + 1 && y == destY)
                        return true
                    if (x == destX && y == destY - 1)
                        return true
                }
                3 -> {
                    if (x == destX - 1 && y == destY)
                        return true
                    if (x == destX && y == destY + 1 &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (x == destX + 1 && y == destY &&
                        (flags[defaultFlag, x, y, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                    if (x == destX && y == destY - 1)
                        return true
                }
            }
        }
        9 -> {
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
    }
    return false
}

private fun reachWallN(
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
    when (shape) {
        0 -> {
            when (rot) {
                0 -> {
                    if (x == destX - srcSize && y <= destY && north >= destY)
                        return true
                    if (destX in x..east && y == destY + 1 &&
                        (flags[defaultFlag, destX, y, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (destX in x..east && y == destY - srcSize &&
                        (flags[defaultFlag, destX, north, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                1 -> {
                    if (destX in x..east && y == destY + 1)
                        return true
                    if (x == destX - srcSize && y <= destY && north >= destY &&
                        (flags[defaultFlag, east, destY, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (x == destX + 1 && y <= destY && north >= destY &&
                        (flags[defaultFlag, x, destY, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                }
                2 -> {
                    if (x == destX + 1 && y <= destY && north >= destY)
                        return true
                    if (destX in x..east && y == destY + 1 &&
                        (flags[defaultFlag, destX, y, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (destX in x..east && y == destY - srcSize &&
                        (flags[defaultFlag, destX, north, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                3 -> {
                    if (destX in x..east && y == destY - srcSize)
                        return true
                    if (x == destX - srcSize && y <= destY && north >= destY &&
                        (flags[defaultFlag, east, destY, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (x == destX + 1 && y <= destY && north >= destY &&
                        (flags[defaultFlag, x, destY, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                }
            }
        }
        2 -> {
            when (rot) {
                0 -> {
                    if (x == destX - srcSize && y <= destY && north >= destY)
                        return true
                    if (destX in x..east && y == destY + 1)
                        return true
                    if (x == destX + 1 && y <= destY && north >= destY &&
                        (flags[defaultFlag, x, destY, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                    if (destX in x..east && y == destY - srcSize &&
                        (flags[defaultFlag, destX, north, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                1 -> {
                    if (x == destX - srcSize && y <= destY && north >= destY &&
                        (flags[defaultFlag, east, destY, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (destX in x..east && y == destY + 1)
                        return true
                    if (x == destX + 1 && y <= destY && north >= destY)
                        return true
                    if (destX in x..east && y == destY - srcSize &&
                        (flags[defaultFlag, destX, north, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                2 -> {
                    if (x == destX - srcSize && y <= destY && north >= destY &&
                        (flags[defaultFlag, east, destY, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (destX in x..east && y == destY + 1 &&
                        (flags[defaultFlag, destX, y, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (x == destX + 1 && y <= destY && north >= destY)
                        return true
                    if (destX in x..east && y == destY - srcSize)
                        return true
                }
                3 -> {
                    if (x == destX - srcSize && y <= destY && north >= destY)
                        return true
                    if (destX in x..east && y == destY + 1 &&
                        (flags[defaultFlag, destX, y, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (x == destX + 1 && y <= destY && north >= destY &&
                        (flags[defaultFlag, x, destY, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                    if (destX in x..east && y == destY - srcSize)
                        return true
                }
            }
        }
        9 -> {
            if (destX in x..east && y == destY + 1 &&
                (flags[defaultFlag, destX, y, z] and CollisionFlag.BLOCK_NORTH) == 0
            ) return true
            if (destX in x..east && y == destY - srcSize &&
                (flags[defaultFlag, destX, north, z] and CollisionFlag.BLOCK_SOUTH) == 0
            ) return true
            if (x == destX - srcSize && y <= destY && north >= destY &&
                (flags[defaultFlag, east, destY, z] and CollisionFlag.BLOCK_WEST) == 0
            ) return true

            return x == destX + 1 && y <= destY && north >= destY &&
                (flags[defaultFlag, x, destY, z] and CollisionFlag.BLOCK_EAST) == 0
        }
    }
    return false
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
