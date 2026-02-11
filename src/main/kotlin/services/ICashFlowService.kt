package org.delcom.services

import org.delcom.data.CashFlowQuery
import org.delcom.data.CashFlowRequest
import org.delcom.entities.CashFlow

interface ICashFlowService {
    fun getAllCashFlows(query: CashFlowQuery): List<CashFlow>
    fun getCashFlowById(id: String): CashFlow?
    fun createCashFlow(req: CashFlowRequest): String
    fun updateCashFlow(id: String, req: CashFlowRequest): Boolean
    fun deleteCashFlow(id: String): Boolean
    fun setupInitialData(): Int

    // Metadata
    fun getDistinctTypes(): List<String>
    fun getDistinctSources(): List<String>
    fun getDistinctLabels(): List<String>
}