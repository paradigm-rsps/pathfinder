package org.rsmod.pathfinder.reach

public interface ReachStrategy {

    public fun reached(
        flags: Array<IntArray?>,
        defaultFlag: Int,
        x: Int,
        y: Int,
        z: Int,
        destX: Int,
        destY: Int,
        destWidth: Int,
        destHeight: Int,
        srcSize: Int,
        rotation: Int,
        shape: Int,
        accessBitMask: Int,
    ): Boolean
}
