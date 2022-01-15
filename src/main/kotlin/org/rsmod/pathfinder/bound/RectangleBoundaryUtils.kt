package org.rsmod.pathfinder.bound

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
        flags: IntArray,
        mapSize: Int,
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
            (flag(flags, mapSize, srcX, srcY) and CollisionFlag.WALL_EAST) == 0 &&
            (accessBitMask and 0x8) == 0
        ) return true

        if (srcX == east + 1 && srcY >= destY && srcY <= north &&
            (flag(flags, mapSize, srcX, srcY) and CollisionFlag.WALL_WEST) == 0 &&
            (accessBitMask and 0x2) == 0
        ) return true

        if (srcY + 1 == destY && srcX >= destX && srcX <= east &&
            (flag(flags, mapSize, srcX, srcY) and CollisionFlag.WALL_NORTH) == 0 &&
            (accessBitMask and 0x4) == 0

        ) return true

        return srcY == north + 1 && srcX >= destX && srcX <= east &&
            (flag(flags, mapSize, srcX, srcY) and CollisionFlag.WALL_SOUTH) == 0 &&
            (accessBitMask and 0x1) == 0
    }

    fun reachRectangleN(
        flags: IntArray,
        mapSize: Int,
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
        if (destEast == srcX && (accessBitMask and 0x2) == 0) {
            val fromY = max(srcY, destY)
            val toY = min(srcNorth, destNorth)
            for (y in fromY until toY) {
                if (flag(flags, mapSize, destEast - 1, y) and CollisionFlag.WALL_EAST == 0) {
                    return true
                }
            }
        } else if (srcEast == destX && (accessBitMask and 0x8) == 0) {
            val fromY = max(srcY, destY)
            val toY = min(srcNorth, destNorth)
            for (y in fromY until toY) {
                if (flag(flags, mapSize, destX, y) and CollisionFlag.WALL_WEST == 0) {
                    return true
                }
            }
        } else if (srcY == destNorth && (accessBitMask and 0x1) == 0) {
            val fromX = max(srcX, destX)
            val toX = min(srcEast, destEast)
            for (x in fromX until toX) {
                if (flag(flags, mapSize, x, destNorth - 1) and CollisionFlag.WALL_NORTH == 0) {
                    return true
                }
            }
        } else if (destY == srcNorth && (accessBitMask and 0x4) == 0) {
            val fromX = max(srcX, destX)
            val toX = min(srcEast, destEast)
            for (x in fromX until toX) {
                if (flag(flags, mapSize, x, destY) and CollisionFlag.WALL_SOUTH == 0) {
                    return true
                }
            }
        }
        return false
    }

    private fun flag(flags: IntArray, width: Int, x: Int, y: Int): Int {
        return flags[(y * width) + x]
    }
}
