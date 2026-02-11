package org.delcom.services

import org.delcom.entities.CashFlow
import org.delcom.repositories.ICashFlowRepository
import org.delcom.data.CashFlowQuery
import org.delcom.helpers.CashFlowHelper // Asumsi ada helper untuk parse tanggal

class CashFlowService(private val repository: ICashFlowRepository) : ICashFlowService {

    override fun getAllCashFlows(query: CashFlowQuery): List<CashFlow> {
        var result = repository.getAll()

        // Logika Filter (Sangat Penting untuk Tes!)
        query.type?.let { t -> result = result.filter { it.type.equals(t, true) } }
        query.source?.let { s -> result = result.filter { it.source.equals(s, true) } }
        query.search?.let { q -> result = result.filter { it.description.contains(q, true) } }
        query.gteAmount?.let { gte -> result = result.filter { it.amount >= gte } }
        query.lteAmount?.let { lte -> result = result.filter { it.amount <= lte } }

        // Filter Label (Contoh: "elektronik,mandi")
        query.labels?.let { qLabels ->
            val filterList = qLabels.split(",")
            result = result.filter { entity ->
                val entityLabels = entity.label.split(",")
                filterList.any { fl -> entityLabels.contains(fl) }
            }
        }

        return result
    }

    override fun getCashFlowById(id: String): CashFlow? = repository.getById(id)

    override fun createCashFlow(type: String, source: String, label: String, amount: Long, description: String): String {
        return repository.create(type, source, label, amount, description)
    }

    override fun createRawCashFlow(id: String, type: String, source: String, label: String, amount: Long, description: String, createdAtStr: String, updatedAtStr: String): String {
        // Konversi String dari JSON ke Instant
        val created = CashFlowHelper.parseToInstant(createdAtStr)
        val updated = CashFlowHelper.parseToInstant(updatedAtStr)
        return repository.createRaw(id, type, source, label, amount, description, created, updated)
    }

    override fun updateCashFlow(id: String, type: String, source: String, label: String, amount: Long, description: String): Boolean {
        return repository.update(id, type, source, label, amount, description)
    }

    override fun removeCashFlow(id: String): Boolean = repository.delete(id)

    // Logika Extensions
    override fun getAvailableTypes() = repository.getAll().map { it.type }.distinct()
    override fun getAvailableSources() = repository.getAll().map { it.source }.distinct()
    override fun getAvailableLabels() = repository.getAll().flatMap { it.label.split(",") }.distinct()
}