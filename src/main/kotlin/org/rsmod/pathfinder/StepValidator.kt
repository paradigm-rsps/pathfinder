package org.rsmod.pathfinder

import org.rsmod.pathfinder.collision.CollisionStrategy

/**
 * @author Kris | 16/03/2022
 */
public class StepValidator(
    private val flags: Array<IntArray?>,
    private val defaultFlag: Int,
) {
    public fun canTravel(
        x: Int,
        y: Int,
        z: Int,
        size: Int,
        offsetX: Int,
        offsetY: Int,
        extraFlagToCheck: Int,
        collision: CollisionStrategy,
    ): Boolean {
        assert(offsetX in -1..1) { "Offset x must be in bounds of -1..1" }
        assert(offsetY in -1..1) { "Offset y must be in bounds of -1..1" }
        assert(offsetX != 0 || offsetY != 0) { "Offset x and y cannot both be 0." }
        val direction = DumbPathFinder.getDirection(offsetX, offsetY)
        return !direction.isBlocked(
            flags,
            defaultFlag,
            extraFlagToCheck,
            x,
            y,
            z,
            collision,
            size
        )
    }
}
