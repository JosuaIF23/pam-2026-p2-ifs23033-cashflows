package org.delcom.services

import org.delcom.entities.CashFlow
import org.delcom.data.CashFlowQuery

interface ICashFlowService {
    fun getAllCashFlows(query: CashFlowQuery): List<CashFlow>
    fun getCashFlowById(id: String): CashFlow?
    fun createCashFlow(type: String, source: String, label: String, amount: Long, description: String): String
    fun createRawCashFlow(id: String, type: String, source: String, label: String, amount: Long, description: String, createdAtStr: String, updatedAtStr: String): String
    fun updateCashFlow(id: String, type: String, source: String, label: String, amount: Long, description: String): Boolean
    fun removeCashFlow(id: String): Boolean

    // Untuk extension endpoints
    fun getAvailableTypes(): List<String>
    fun getAvailableSources(): List<String>
    fun getAvailableLabels(): List<String>
}