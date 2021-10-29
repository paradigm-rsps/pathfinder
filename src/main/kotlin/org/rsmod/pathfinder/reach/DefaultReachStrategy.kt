package org.rsmod.pathfinder.reach

import org.rsmod.pathfinder.bound.reachExclusiveRectangle
import org.rsmod.pathfinder.bound.reachRectangle
import org.rsmod.pathfinder.bound.reachWall
import org.rsmod.pathfinder.bound.reachWallDeco

private const val WALL_STRATEGY = 0
private const val WALL_DECO_STRATEGY = 1
private const val RECTANGLE_STRATEGY = 2
private const val NO_STRATEGY = 3
private const val RECTANGLE_EXCLUSIVE_STRATEGY = 4

public object DefaultReachStrategy : ReachStrategy {

    override fun reached(
        flags: IntArray,
        localSrcX: Int,
        localSrcY: Int,
        localDestX: Int,
        localDestY: Int,
        destWidth: Int,
        destHeight: Int,
        srcSize: Int,
        rotation: Int,
        shape: Int,
        accessBitMask: Int,
        searchMapSize: Int
    ): Boolean {
        val exitStrategy = shape.exitStrategy
        if (exitStrategy != RECTANGLE_EXCLUSIVE_STRATEGY && localSrcX == localDestX && localSrcY == localDestY) {
            return true
        }

        return when (exitStrategy) {
            WALL_STRATEGY -> reachWall(
                flags,
                searchMapSize,
                localSrcX,
                localSrcY,
                localDestX,
                localDestY,
                srcSize,
                shape,
                rotation
            )
            WALL_DECO_STRATEGY -> reachWallDeco(
                flags,
                searchMapSize,
                localSrcX,
                localSrcY,
                localDestX,
                localDestY,
                srcSize,
                shape,
                rotation
            )
            RECTANGLE_STRATEGY -> reachRectangle(
                flags,
                searchMapSize,
                accessBitMask,
                localSrcX,
                localSrcY,
                localDestX,
                localDestY,
                srcSize,
                destWidth,
                destHeight
            )
            RECTANGLE_EXCLUSIVE_STRATEGY -> reachExclusiveRectangle(
                flags,
                searchMapSize,
                accessBitMask,
                localSrcX,
                localSrcY,
                localDestX,
                localDestY,
                srcSize,
                destWidth,
                destHeight
            )
            else -> false
        }
    }

    private val Int.exitStrategy: Int
        get() = when {
            this == -2 -> RECTANGLE_EXCLUSIVE_STRATEGY
            this == -1 -> NO_STRATEGY
            this in 0..3 || this == 9 -> WALL_STRATEGY
            this < 9 -> WALL_DECO_STRATEGY
            this in 10..11 || this == 22 -> RECTANGLE_STRATEGY
            else -> NO_STRATEGY
        }
}
