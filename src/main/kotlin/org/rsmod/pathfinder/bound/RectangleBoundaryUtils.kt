package org.rsmod.pathfinder.bound

import org.rsmod.pathfinder.flag.AccessBitFlag
import org.rsmod.pathfinder.flag.CollisionFlag
import kotlin.math.max
import kotlin.math.min

/**
 * @author Kris | 12/09/2021
 */
internal object RectangleBoundaryUtils {
    fun collides(
        srcX: Int,
        srcY: Int,
        destX: Int,
        destY: Int,
        srcWidth: Int,
        srcHeight: Int,
        destWidth: Int,
        destHeight: Int
    ): Boolean = if (srcX >= destX + destWidth || srcX + srcWidth <= destX) {
        false
    } else {
        srcY < destY + destHeight && destY < srcHeight + srcY
    }

    fun reachRectangle1(
        flags: Array<IntArray?>,
        defaultFlag: Int,
        baseX: Int,
        baseY: Int,
        z: Int,
        accessBitMask: Int,
        srcX: Int,
        srcY: Int,
        destX: Int,
        destY: Int,
        destWidth: Int,
        destHeight: Int
    ): Boolean {
        val east = destX + destWidth - 1
        val north = destY + destHeight - 1

        if (srcX in destX..east && srcY in destY..north)
            return false

        if (srcX == destX - 1 && srcY >= destY && srcY <= north &&
            (flags[defaultFlag, baseX, baseY, srcX, srcY, z] and CollisionFlag.WALL_EAST) == 0 &&
            (accessBitMask and AccessBitFlag.BLOCK_WEST) == 0
        ) return true

        if (srcX == east + 1 && srcY >= destY && srcY <= north &&
            (flags[defaultFlag, baseX, baseY, srcX, srcY, z] and CollisionFlag.WALL_WEST) == 0 &&
            (accessBitMask and AccessBitFlag.BLOCK_EAST) == 0
        ) return true

        if (srcY + 1 == destY && srcX >= destX && srcX <= east &&
            (flags[defaultFlag, baseX, baseY, srcX, srcY, z] and CollisionFlag.WALL_NORTH) == 0 &&
            (accessBitMask and AccessBitFlag.BLOCK_SOUTH) == 0

        ) return true

        return srcY == north + 1 && srcX >= destX && srcX <= east &&
            (flags[defaultFlag, baseX, baseY, srcX, srcY, z] and CollisionFlag.WALL_SOUTH) == 0 &&
            (accessBitMask and AccessBitFlag.BLOCK_NORTH) == 0
    }

    fun reachRectangleN(
        flags: Array<IntArray?>,
        defaultFlag: Int,
        baseX: Int,
        baseY: Int,
        z: Int,
        accessBitMask: Int,
        srcX: Int,
        srcY: Int,
        destX: Int,
        destY: Int,
        srcWidth: Int,
        srcHeight: Int,
        destWidth: Int,
        destHeight: Int
    ): Boolean {
        val srcEast = srcX + srcWidth
        val srcNorth = srcHeight + srcY
        val destEast = destWidth + destX
        val destNorth = destHeight + destY
        if (destEast == srcX && (accessBitMask and AccessBitFlag.BLOCK_EAST) == 0) {
            val fromY = max(srcY, destY)
            val toY = min(srcNorth, destNorth)
            for (y in fromY until toY) {
                if (flags[defaultFlag, baseX, baseY, destEast - 1, y, z] and CollisionFlag.WALL_EAST == 0) {
                    return true
                }
            }
        } else if (srcEast == destX && (accessBitMask and AccessBitFlag.BLOCK_WEST) == 0) {
            val fromY = max(srcY, destY)
            val toY = min(srcNorth, destNorth)
            for (y in fromY until toY) {
                if (flags[defaultFlag, baseX, baseY, destX, y, z] and CollisionFlag.WALL_WEST == 0) {
                    return true
                }
            }
        } else if (srcY == destNorth && (accessBitMask and AccessBitFlag.BLOCK_NORTH) == 0) {
            val fromX = max(srcX, destX)
            val toX = min(srcEast, destEast)
            for (x in fromX until toX) {
                if (flags[defaultFlag, baseX, baseY, x, destNorth - 1, z] and CollisionFlag.WALL_NORTH == 0) {
                    return true
                }
            }
        } else if (destY == srcNorth && (accessBitMask and AccessBitFlag.BLOCK_SOUTH) == 0) {
            val fromX = max(srcX, destX)
            val toX = min(srcEast, destEast)
            for (x in fromX until toX) {
                if (flags[defaultFlag, baseX, baseY, x, destY, z] and CollisionFlag.WALL_SOUTH == 0) {
                    return true
                }
            }
        }
        return false
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
        return ((x shr 3) and 0x7FF) or (((y shr 3) and 0x7FF) shl 11) or ((z and 0x3) shl 22)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun getIndexInZone(x: Int, y: Int): Int {
        return (x and 0x7) or ((y and 0x7) shl 3)
    }
}
