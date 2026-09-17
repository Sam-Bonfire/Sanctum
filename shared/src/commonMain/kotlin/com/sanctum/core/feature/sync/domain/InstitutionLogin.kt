package com.sanctum.core.feature.sync.domain

import kotlinx.serialization.Serializable

@Serializable
data class InstitutionLogin(
    val organizationId: String,
    val inviteCode: String,
    val role: String = "member",
)

val INSTITUTION_ROLES = setOf("admin", "clergy", "member")

fun InstitutionLogin.isValid(): Boolean =
    organizationId.isNotBlank() && inviteCode.trim().length >= 6 && role in INSTITUTION_ROLES

fun InstitutionLogin.isStaff(): Boolean = role == "admin" || role == "clergy"
