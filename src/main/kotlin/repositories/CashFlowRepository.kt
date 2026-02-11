package org.delcom.repositories

import org.delcom.entities.CashFlow
import kotlin.time.Clock
import kotlin.time.Instant

class CashFlowRepository : ICashFlowRepository {
    private val data = mutableListOf<CashFlow>()

    override fun getAll(): List<CashFlow> = data

    override fun getById(id: String): CashFlow? = data.find { it.id == id }

    override fun create(type: String, source: String, label: String, amount: Long, description: String): String {
        val newCashFlow = CashFlow(
            type = type, source = source, label = label,
            amount = amount, description = description
        )
        data.add(newCashFlow)
        return newCashFlow.id
    }

    override fun createRaw(id: String, type: String, source: String, label: String, amount: Long, description: String, createdAt: Instant, updatedAt: Instant): String {
        val rawCashFlow = CashFlow(id, type, source, label, amount, description, createdAt, updatedAt)
        data.add(rawCashFlow)
        return rawCashFlow.id
    }

    override fun update(id: String, type: String, source: String, label: String, amount: Long, description: String): Boolean {
        val target = getById(id) ?: return false
        target.type = type
        target.source = source
        target.label = label
        target.amount = amount
        target.description = description
        target.updatedAt = Clock.System.now()
        return true
    }

    override fun delete(id: String): Boolean = data.removeIf { it.id == id }
}