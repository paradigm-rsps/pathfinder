/*
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
@file:Suppress("MemberVisibilityCanBePrivate")

package org.rsmod.pathfinder

import org.rsmod.pathfinder.flag.CollisionFlag
import org.rsmod.pathfinder.flag.CollisionFlag.OBJECT_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_EAST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_WEST_PROJECTILE_BLOCKER
import kotlin.math.abs

/* original RuneLite code revised by Scu11 */
public class LineValidator(public val searchMapSize: Int = DEFAULT_SEARCH_MAP_SIZE) {

    public fun hasLineOfSight(
        flags: IntArray,
        srcX: Int,
        srcY: Int,
        destX: Int,
        destY: Int,
        srcSize: Int = 1,
        destWidth: Int = 0,
        destHeight: Int = 0
    ): Boolean {
        val route = rayCast(
            flags,
            srcX,
            srcY,
            destX,
            destY,
            srcSize,
            destWidth,
            destHeight,
            SIGHT_BLOCKED_WEST,
            SIGHT_BLOCKED_EAST,
            SIGHT_BLOCKED_SOUTH,
            SIGHT_BLOCKED_NORTH
        )
        return route.success
    }

    public fun hasLineOfWalk(
        flags: IntArray,
        srcX: Int,
        srcY: Int,
        destX: Int,
        destY: Int,
        srcSize: Int = 1,
        destWidth: Int = 0,
        destHeight: Int = 0
    ): Boolean {
        val route = rayCast(
            flags,
            srcX,
            srcY,
            destX,
            destY,
            srcSize,
            destWidth,
            destHeight,
            WALK_BLOCKED_WEST,
            WALK_BLOCKED_EAST,
            WALK_BLOCKED_SOUTH,
            WALK_BLOCKED_NORTH
        )
        return route.success
    }

    private fun rayCast(
        flags: IntArray,
        srcX: Int,
        srcY: Int,
        destX: Int,
        destY: Int,
        srcSize: Int = 1,
        destWidth: Int = 0,
        destHeight: Int = 0,
        flagWest: Int,
        flagEast: Int,
        flagSouth: Int,
        flagNorth: Int,
    ): Route {
        val halfMap = searchMapSize / 2
        val baseX = srcX - halfMap
        val baseY = srcY - halfMap
        val localSrcX = srcX - baseX
        val localSrcY = srcY - baseY
        val localDestX = destX - baseX
        val localDestY = destY - baseY

        val startX = coordinate(localSrcX, localDestX, srcSize)
        val startY = coordinate(localSrcY, localDestY, srcSize)

        if (flags.isFlagged(startX, startY, CollisionFlag.OBJECT)) return Route(emptyList(), alternative = false, success = false)

        val endX = coordinate(localDestX, localSrcX, destWidth)
        val endY = coordinate(localDestY, localSrcY, destHeight)

        if (startX == endX && startY == endY) return Route(emptyList(), alternative = false, success = true)

        val deltaX = endX - startX
        val deltaY = endY - startY

        val travelEast = deltaX >= 0
        val travelNorth = deltaY >= 0

        val xFlags = if (travelEast) flagWest else flagEast
        val yFlags = if (travelNorth) flagSouth else flagNorth

        val coords = mutableListOf<RouteCoordinates>()

        if (abs(deltaX) > abs(deltaY)) {
            val offsetX = if (travelEast) 1 else -1
            val offsetY = if (travelNorth) 0 else -1

            var scaledY = scaleUp(startY) + HALF_TILE + offsetY
            val tangent = scaleUp(deltaY) / abs(deltaX)

            var currX = startX
            while (currX != endX) {
                currX += offsetX
                val currY = scaleDown(scaledY)

                if (flags.isFlagged(currX, currY, xFlags)) {
                    return Route(coords, alternative = false, success = false)
                }

                scaledY += tangent

                val nextY = scaleDown(scaledY)
                if (nextY != currY && flags.isFlagged(currX, nextY, yFlags)) {
                    return Route(coords, alternative = false, success = false)
                }
            }
        } else {
            val offsetX = if (travelEast) 0 else -1
            val offsetY = if (travelNorth) 1 else -1

            var scaledX = scaleUp(startX) + HALF_TILE + offsetX
            val tangent = scaleUp(deltaX) / abs(deltaY)

            var currY = startY
            while (currY != endY) {
                currY += offsetY
                val currX = scaleDown(scaledX)

                if (flags.isFlagged(currX, currY, yFlags)) {
                    return Route(coords, alternative = false, success = false)
                }

                scaledX += tangent

                val nextX = scaleDown(scaledX)
                if (nextX != currX && flags.isFlagged(nextX, currY, xFlags)) {
                    return Route(coords, alternative = false, success = false)
                }
            }
        }
        return Route(coords, alternative = false, success = true)
    }

    private fun collides(
        srcX: Int,
        srcY: Int,
        destX: Int,
        destY: Int,
        srcSize: Int,
        destWidth: Int,
        destHeight: Int
    ): Boolean {
        // Is our eastern side to the west of the target's western side?
        if (srcX + srcSize < destX) return false
        // Is our northern side to the south of the target's southern side?
        if (srcY + srcSize < destY) return false
        // Is our western side to the east of the target's eastern side?
        if (srcX > destX + destWidth) return false
        // Is our southern side to the north of the target's northern side?
        if (srcY > destY + destHeight) return false
        // If none of the four checks return false, the two rectangles must be colliding
        return true
    }

    private fun coordinate(a: Int, b: Int, size: Int): Int {
        return when {
            a >= b -> a
            a + size - 1 <= b -> a + size - 1
            else -> b
        }
    }

    private fun IntArray.isFlagged(x: Int, y: Int, flags: Int): Boolean {
        return (this[x, y] and flags) != 0
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline operator fun IntArray.get(x: Int, y: Int): Int {
        val index = (y * searchMapSize) + x
        return this[index]
    }

    private companion object {

        private const val SIGHT_BLOCKED_NORTH = OBJECT_PROJECTILE_BLOCKER or WALL_NORTH_PROJECTILE_BLOCKER
        private const val SIGHT_BLOCKED_EAST = OBJECT_PROJECTILE_BLOCKER or WALL_EAST_PROJECTILE_BLOCKER
        private const val SIGHT_BLOCKED_SOUTH = OBJECT_PROJECTILE_BLOCKER or WALL_SOUTH_PROJECTILE_BLOCKER
        private const val SIGHT_BLOCKED_WEST = OBJECT_PROJECTILE_BLOCKER or WALL_WEST_PROJECTILE_BLOCKER

        private const val WALK_BLOCKED_NORTH =
            CollisionFlag.WALL_NORTH or CollisionFlag.OBJECT or CollisionFlag.FLOOR_DECORATION or
                CollisionFlag.UNKNOWN_BIT_20 or CollisionFlag.FLOOR
        private const val WALK_BLOCKED_EAST =
            CollisionFlag.WALL_EAST or CollisionFlag.OBJECT or CollisionFlag.FLOOR_DECORATION or
                CollisionFlag.UNKNOWN_BIT_20 or CollisionFlag.FLOOR
        private const val WALK_BLOCKED_SOUTH =
            CollisionFlag.WALL_SOUTH or CollisionFlag.OBJECT or CollisionFlag.FLOOR_DECORATION or
                CollisionFlag.UNKNOWN_BIT_20 or CollisionFlag.FLOOR
        private const val WALK_BLOCKED_WEST =
            CollisionFlag.WALL_WEST or CollisionFlag.OBJECT or CollisionFlag.FLOOR_DECORATION or
                CollisionFlag.UNKNOWN_BIT_20 or CollisionFlag.FLOOR

        private const val SCALE = 16
        private val HALF_TILE = scaleUp(tiles = 1) / 2

        private fun scaleUp(tiles: Int) = tiles shl SCALE

        private fun scaleDown(tiles: Int) = tiles ushr SCALE
    }
}
