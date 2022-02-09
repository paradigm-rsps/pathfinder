@file:Suppress("DuplicatedCode")

package org.rsmod.pathfinder.bound

import org.rsmod.pathfinder.flag.CollisionFlag

internal fun reachWall(
    flags: Array<IntArray?>,
    defaultFlags: IntArray,
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
    srcSize == 1 && localSrcX == localDestX && localSrcY == localDestY -> true
    srcSize != 1 && localDestX >= localSrcX && srcSize + localSrcX - 1 >= localDestX &&
        srcSize + localDestY - 1 >= localDestY -> true
    srcSize == 1 -> reachWall1(
        flags,
        defaultFlags,
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
    else -> reachWallN(
        flags,
        defaultFlags,
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

private fun reachWall1(
    flags: Array<IntArray?>,
    defaultFlags: IntArray,
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
    when (shape) {
        0 -> {
            when (rot) {
                0 -> {
                    if (localSrcX == localDestX - 1 && localSrcY == localDestY)
                        return true
                    if (localSrcX == localDestX && localSrcY == localDestY + 1 &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (localSrcX == localDestX && localSrcY == localDestY - 1 &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                1 -> {
                    if (localSrcX == localDestX && localSrcY == localDestY + 1)
                        return true
                    if (localSrcX == localDestX - 1 && localSrcY == localDestY &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (localSrcX == localDestX + 1 && localSrcY == localDestY &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                }
                2 -> {
                    if (localSrcX == localDestX + 1 && localSrcY == localDestY)
                        return true
                    if (localSrcX == localDestX && localSrcY == localDestY + 1 &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (localSrcX == localDestX && localSrcY == localDestY - 1 &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                3 -> {
                    if (localSrcX == localDestX && localSrcY == localDestY - 1)
                        return true
                    if (localSrcX == localDestX - 1 && localSrcY == localDestY &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (localSrcX == localDestX + 1 && localSrcY == localDestY &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                }
            }
        }
        2 -> {
            when (rot) {
                0 -> {
                    if (localSrcX == localDestX - 1 && localSrcY == localDestY)
                        return true
                    if (localSrcX == localDestX && localSrcY == localDestY + 1)
                        return true
                    if (localSrcX == localDestX + 1 && localSrcY == localDestY &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                    if (localSrcX == localDestX && localSrcY == localDestY - 1 &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                1 -> {
                    if (localSrcX == localDestX - 1 && localSrcY == localDestY &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (localSrcX == localDestX && localSrcY == localDestY + 1)
                        return true
                    if (localSrcX == localDestX + 1 && localSrcY == localDestY)
                        return true
                    if (localSrcX == localDestX && localSrcY == localDestY - 1 &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                2 -> {
                    if (localSrcX == localDestX - 1 && localSrcY == localDestY &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (localSrcX == localDestX && localSrcY == localDestY + 1 &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (localSrcX == localDestX + 1 && localSrcY == localDestY)
                        return true
                    if (localSrcX == localDestX && localSrcY == localDestY - 1)
                        return true
                }
                3 -> {
                    if (localSrcX == localDestX - 1 && localSrcY == localDestY)
                        return true
                    if (localSrcX == localDestX && localSrcY == localDestY + 1 &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (localSrcX == localDestX + 1 && localSrcY == localDestY &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                    if (localSrcX == localDestX && localSrcY == localDestY - 1)
                        return true
                }
            }
        }
        9 -> {
            if (localSrcX == localDestX && localSrcY == localDestY + 1 &&
                (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_SOUTH) == 0
            ) return true
            if (localSrcX == localDestX && localSrcY == localDestY - 1 &&
                (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_NORTH) == 0
            ) return true
            if (localSrcX == localDestX - 1 && localSrcY == localDestY &&
                (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_EAST) == 0
            ) return true

            return localSrcX == localDestX + 1 && localSrcY == localDestY &&
                (flags[defaultFlags, baseX, baseY, localSrcX, localSrcY, z] and CollisionFlag.WALL_WEST) == 0
        }
    }
    return false
}

private fun reachWallN(
    flags: Array<IntArray?>,
    defaultFlags: IntArray,
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
    when (shape) {
        0 -> {
            when (rot) {
                0 -> {
                    if (localSrcX == localDestX - srcSize && localSrcY <= localDestY && north >= localDestY)
                        return true
                    if (localDestX in localSrcX..east && localSrcY == localDestY + 1 &&
                        (flags[defaultFlags, baseX, baseY, localDestX, localSrcY, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (localDestX in localSrcX..east && localSrcY == localDestY - srcSize &&
                        (flags[defaultFlags, baseX, baseY, localDestX, north, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                1 -> {
                    if (localDestX in localSrcX..east && localSrcY == localDestY + 1)
                        return true
                    if (localSrcX == localDestX - srcSize && localSrcY <= localDestY && north >= localDestY &&
                        (flags[defaultFlags, baseX, baseY, east, localDestY, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (localSrcX == localDestX + 1 && localSrcY <= localDestY && north >= localDestY &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localDestY, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                }
                2 -> {
                    if (localSrcX == localDestX + 1 && localSrcY <= localDestY && north >= localDestY)
                        return true
                    if (localDestX in localSrcX..east && localSrcY == localDestY + 1 &&
                        (flags[defaultFlags, baseX, baseY, localDestX, localSrcY, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (localDestX in localSrcX..east && localSrcY == localDestY - srcSize &&
                        (flags[defaultFlags, baseX, baseY, localDestX, north, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                3 -> {
                    if (localDestX in localSrcX..east && localSrcY == localDestY - srcSize)
                        return true
                    if (localSrcX == localDestX - srcSize && localSrcY <= localDestY && north >= localDestY &&
                        (flags[defaultFlags, baseX, baseY, east, localDestY, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (localSrcX == localDestX + 1 && localSrcY <= localDestY && north >= localDestY &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localDestY, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                }
            }
        }
        2 -> {
            when (rot) {
                0 -> {
                    if (localSrcX == localDestX - srcSize && localSrcY <= localDestY && north >= localDestY)
                        return true
                    if (localDestX in localSrcX..east && localSrcY == localDestY + 1)
                        return true
                    if (localSrcX == localDestX + 1 && localSrcY <= localDestY && north >= localDestY &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localDestY, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                    if (localDestX in localSrcX..east && localSrcY == localDestY - srcSize &&
                        (flags[defaultFlags, baseX, baseY, localDestX, north, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                1 -> {
                    if (localSrcX == localDestX - srcSize && localSrcY <= localDestY && north >= localDestY &&
                        (flags[defaultFlags, baseX, baseY, east, localDestY, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (localDestX in localSrcX..east && localSrcY == localDestY + 1)
                        return true
                    if (localSrcX == localDestX + 1 && localSrcY <= localDestY && north >= localDestY)
                        return true
                    if (localDestX in localSrcX..east && localSrcY == localDestY - srcSize &&
                        (flags[defaultFlags, baseX, baseY, localDestX, north, z] and CollisionFlag.BLOCK_SOUTH) == 0
                    ) return true
                }
                2 -> {
                    if (localSrcX == localDestX - srcSize && localSrcY <= localDestY && north >= localDestY &&
                        (flags[defaultFlags, baseX, baseY, east, localDestY, z] and CollisionFlag.BLOCK_WEST) == 0
                    ) return true
                    if (localDestX in localSrcX..east && localSrcY == localDestY + 1 &&
                        (flags[defaultFlags, baseX, baseY, localDestX, localSrcY, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (localSrcX == localDestX + 1 && localSrcY <= localDestY && north >= localDestY)
                        return true
                    if (localDestX in localSrcX..east && localSrcY == localDestY - srcSize)
                        return true
                }
                3 -> {
                    if (localSrcX == localDestX - srcSize && localSrcY <= localDestY && north >= localDestY)
                        return true
                    if (localDestX in localSrcX..east && localSrcY == localDestY + 1 &&
                        (flags[defaultFlags, baseX, baseY, localDestX, localSrcY, z] and CollisionFlag.BLOCK_NORTH) == 0
                    ) return true
                    if (localSrcX == localDestX + 1 && localSrcY <= localDestY && north >= localDestY &&
                        (flags[defaultFlags, baseX, baseY, localSrcX, localDestY, z] and CollisionFlag.BLOCK_EAST) == 0
                    ) return true
                    if (localDestX in localSrcX..east && localSrcY == localDestY - srcSize)
                        return true
                }
            }
        }
        9 -> {
            if (localDestX in localSrcX..east && localSrcY == localDestY + 1 &&
                (flags[defaultFlags, baseX, baseY, localDestX, localSrcY, z] and CollisionFlag.BLOCK_NORTH) == 0
            ) return true
            if (localDestX in localSrcX..east && localSrcY == localDestY - srcSize &&
                (flags[defaultFlags, baseX, baseY, localDestX, north, z] and CollisionFlag.BLOCK_SOUTH) == 0
            ) return true
            if (localSrcX == localDestX - srcSize && localSrcY <= localDestY && north >= localDestY &&
                (flags[defaultFlags, baseX, baseY, east, localDestY, z] and CollisionFlag.BLOCK_WEST) == 0
            ) return true

            return localSrcX == localDestX + 1 && localSrcY <= localDestY && north >= localDestY &&
                (flags[defaultFlags, baseX, baseY, localSrcX, localDestY, z] and CollisionFlag.BLOCK_EAST) == 0
        }
    }
    return false
}

@Suppress("NOTHING_TO_INLINE")
private inline operator fun Array<IntArray?>.get(
    defaultFlags: IntArray,
    baseX: Int,
    baseY: Int,
    localX: Int,
    localY: Int,
    z: Int
): Int {
    val x = baseX + localX
    val y = baseY + localY
    val zone = this[getZoneIndex(x, y, z)] ?: defaultFlags
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
