package com.sanctum.core.feature.rosary.domain

expect fun triggerJapaMalaHaptics()

class JapaMala {
    var beads: Int = 0
        private set
    var laps: Int = 0
        private set

    fun increment() {
        beads++
        if (beads == 108) {
            beads = 0
            laps++
        }
        triggerJapaMalaHaptics()
    }
}
