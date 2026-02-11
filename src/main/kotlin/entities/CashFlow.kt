package org.delcom.entities

import kotlin.time.Instant
import kotlin.time.Clock
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class CashFlow(
    val id: String = UUID.randomUUID().toString(),
    var type: String,
    var source: String,
    var label: String,
    var amount: Long,
    var description: String,

    @Contextual
    val createdAt: Instant = Clock.System.now(),
    @Contextual
    var updatedAt: Instant = Clock.System.now()
)