package com.sanctum.core.feature.groups.domain

import kotlinx.serialization.Serializable

@Serializable
data class StudyGroup(
    val id: String,
    val name: String,
    val flavorIds: List<String>,
    val memberIds: List<String> = emptyList(),
    val maxMembers: Int = 50,
)

fun StudyGroup.isCrossFlavor(): Boolean = flavorIds.distinct().size > 1

fun StudyGroup.canJoin(memberId: String): Boolean =
    !memberIds.contains(memberId) && memberIds.size < maxMembers

fun StudyGroup.join(memberId: String): StudyGroup {
    require(canJoin(memberId)) { "Cannot join group" }
    return copy(memberIds = memberIds + memberId)
}
