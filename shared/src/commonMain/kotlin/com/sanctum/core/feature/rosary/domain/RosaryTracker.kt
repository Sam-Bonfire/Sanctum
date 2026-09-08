package com.sanctum.core.feature.rosary.domain

class RosaryTracker(val steps: List<RosaryStep>) {
    init {
        require(steps.isNotEmpty()) { "steps must not be empty" }
    }

    var index: Int = 0
        private set

    /** Null once the rosary is complete; check [isComplete] before reading. */
    val current: RosaryStep? get() = if (isComplete) null else steps[index]

    val completedCount: Int get() = index.coerceAtMost(steps.size)

    val progress: Float get() = completedCount.toFloat() / steps.size.toFloat()

    val isComplete: Boolean get() = index >= steps.size

    /** Returns false when already complete. */
    fun advance(): Boolean {
        if (isComplete) return false
        index++
        return true
    }

    fun retreat() {
        if (index > 0) index--
    }

    fun reset() {
        index = 0
    }

    companion object {
        fun forMystery(set: RosaryMysterySet): RosaryTracker = RosaryTracker(buildRosarySteps(set))
    }
}
