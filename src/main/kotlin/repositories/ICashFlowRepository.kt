package org.delcom.repositories

import org.delcom.entities.CashFlow
import kotlin.time.Instant

interface ICashFlowRepository {
    fun getAll(): List<CashFlow>
    fun getById(id: String): CashFlow?
    fun create(type: String, source: String, label: String, amount: Long, description: String): String
    // Khusus untuk setupData agar ID dan Waktu tidak berubah dari JSON
    fun createRaw(id: String, type: String, source: String, label: String, amount: Long, description: String, createdAt: Instant, updatedAt: Instant): String
    fun update(id: String, type: String, source: String, label: String, amount: Long, description: String): Boolean
    fun delete(id: String): Boolean
}