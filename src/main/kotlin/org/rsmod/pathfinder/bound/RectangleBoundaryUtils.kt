package org.rsmod.pathfinder.bound

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
            (flag(flags, mapSize, srcX, srcY) and 0x8) == 0 &&
            (accessBitMask and 0x8) == 0
        ) return true

        if (srcX == east + 1 && srcY >= destY && srcY <= north &&
            (flag(flags, mapSize, srcX, srcY) and 0x80) == 0 &&
            (accessBitMask and 0x2) == 0
        ) return true

        if (srcY + 1 == destY && srcX >= destX && srcX <= east &&
            (flag(flags, mapSize, srcX, srcY) and 0x2) == 0 &&
            (accessBitMask and 0x4) == 0

        ) return true

        return srcY == north + 1 && srcX >= destX && srcX <= east &&
            (flag(flags, mapSize, srcX, srcY) and 0x20) == 0 &&
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
        if (srcX in destX until destEast) {
            if (destY == srcNorth && (accessBitMask and 0x4) == 0) {
                val minEast = min(srcEast, destEast)
                for (x in srcX until minEast) {
                    if ((flag(flags, mapSize, x, srcNorth - 1) and 0x2) == 0) {
                        return true
                    }
                }
            } else if (destNorth == srcY && (accessBitMask and 0x1) == 0) {
                val minEastX = min(srcEast, destEast)
                for (x in srcX until minEastX) {
                    if ((flag(flags, mapSize, x, srcY) and 0x20) == 0) {
                        return true
                    }
                }
            }
        } else if (srcEast in (destX + 1)..destEast) {
            if (destY == srcNorth && (accessBitMask and 0x4) == 0) {
                for (x in destX until srcEast) {
                    if ((flag(flags, mapSize, x, srcNorth - 1) and 0x2) == 0) {
                        return true
                    }
                }
            } else if (srcY == destNorth && (accessBitMask and 0x1) == 0) {
                for (x in destX until srcEast) {
                    if ((flag(flags, mapSize, x, srcY) and 0x2) == 0) {
                        return true
                    }
                }
            }
        } else if (srcY in destY until destNorth) {
            if (srcEast == destX && (accessBitMask and 0x8) == 0) {
                val minNorthY = min(srcNorth, destNorth)
                for (y in srcY until minNorthY) {
                    if ((flag(flags, mapSize, srcEast - 1, y) and 0x8) == 0) {
                        return true
                    }
                }
            } else if (destEast == srcX && (accessBitMask and 0x2) == 0) {
                val minNorthY = min(srcNorth, destNorth)
                for (y in srcY until minNorthY) {
                    if ((flag(flags, mapSize, srcX, y) and 0x80) == 0) {
                        return true
                    }
                }
            }
        } else if (srcNorth in (destY + 1)..destNorth) {
            if (destX == srcEast && (accessBitMask and 0x8) == 0) {
                for (y in destY until srcNorth) {
                    if ((flag(flags, mapSize, srcEast - 1, y) and 0x8) == 0) {
                        return true
                    }
                }
            } else if (destEast == srcX && (accessBitMask and 0x2) == 0) {
                for (y in destY until srcNorth) {
                    if ((flag(flags, mapSize, srcX, y) and 0x80) == 0) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun flag(flags: IntArray, width: Int, x: Int, y: Int): Int {
        return flags[(y * width) + x]
    }
}
