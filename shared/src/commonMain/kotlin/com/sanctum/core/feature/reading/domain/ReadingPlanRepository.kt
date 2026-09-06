package com.sanctum.core.feature.reading.domain

interface ReadingPlanRepository {
    fun getAvailablePlans(): List<ReadingPlan>
    fun getEnrolledPlanIds(): Set<String>
    fun enroll(planId: String)
    fun unenroll(planId: String)
    fun getProgress(planId: String): ReadingProgress
    fun isCheckpointCompleted(planId: String, checkpointKey: String): Boolean
    fun setCheckpointCompleted(planId: String, checkpointKey: String, completed: Boolean)
}
