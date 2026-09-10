package com.sanctum.core.feature.reflection.domain

/**
 * Silent reflection countdown. The timer never ticks itself: callers pass
 * [nowMs] (e.g. from a UI ticker or background callback), which keeps the
 * domain pure and unit-testable without coroutines or a clock.
 *
 * Completion is level-reported: poll [isComplete] on every tick. A late tick
 * only delays handling, never skips it. After completion the timer stays
 * complete until [reset] — [start] does not implicitly restart.
 */
class ReflectionTimer(val totalMs: Long) {
    init {
        require(totalMs > 0) { "totalMs must be > 0" }
    }

    private var accumulatedMs: Long = 0
    private var runningSinceMs: Long? = null

    val isRunning: Boolean get() = runningSinceMs != null

    /** Starts or resumes; no-op when already running. */
    fun start(nowMs: Long) {
        if (runningSinceMs == null) runningSinceMs = nowMs
    }

    fun pause(nowMs: Long) {
        val since = runningSinceMs ?: return
        accumulatedMs = (accumulatedMs + (nowMs - since).coerceAtLeast(0)).coerceAtMost(totalMs)
        runningSinceMs = null
    }

    fun reset() {
        accumulatedMs = 0
        runningSinceMs = null
    }

    fun elapsedMs(nowMs: Long): Long {
        val since = runningSinceMs
        val live = if (since != null) (nowMs - since).coerceAtLeast(0) else 0
        return (accumulatedMs + live).coerceAtMost(totalMs)
    }

    fun remainingMs(nowMs: Long): Long = totalMs - elapsedMs(nowMs)

    fun isComplete(nowMs: Long): Boolean = remainingMs(nowMs) == 0L

    fun progress(nowMs: Long): Float = (elapsedMs(nowMs).toFloat() / totalMs).coerceIn(0f, 1f)
}
