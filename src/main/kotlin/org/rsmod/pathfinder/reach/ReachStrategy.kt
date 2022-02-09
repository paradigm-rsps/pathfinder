package org.rsmod.pathfinder.reach

public interface ReachStrategy {

    public fun reached(
        flags: Array<IntArray?>,
        defaultFlags: IntArray,
        baseX: Int,
        baseY: Int,
        z: Int,
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
    ): Boolean
}
