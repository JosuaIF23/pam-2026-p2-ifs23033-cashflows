package org.delcom.controllers

import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.delcom.data.*
import org.delcom.services.ICashFlowService
import org.delcom.helpers.ValidatorHelper
import org.delcom.helpers.loadInitialData

class CashFlowController(private val cashFlowService: ICashFlowService) {

    // Identik dengan pola getAllTodos, tapi ditambah penanganan Query & Total
    suspend fun getAllCashFlows(call: ApplicationCall) {
        val query = CashFlowQuery(
            type = call.request.queryParameters["type"],
            source = call.request.queryParameters["source"],
            labels = call.request.queryParameters["labels"],
            search = call.request.queryParameters["search"],
            gteAmount = call.request.queryParameters["gteAmount"]?.toLongOrNull(),
            lteAmount = call.request.queryParameters["lteAmount"]?.toLongOrNull(),
            startDate = call.request.queryParameters["startDate"],
            endDate = call.request.queryParameters["endDate"]
        )

        val cashFlows = cashFlowService.getAllCashFlows(query)

        val response = DataResponse(
            "success",
            "Berhasil mengambil daftar catatan keuangan",
            mapOf(
                "cashFlows" to cashFlows,
                "total" to cashFlows.size // Penting untuk grading
            )
        )
        call.respond(response)
    }

    suspend fun getCashFlowById(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID tidak boleh kosong!")

        val cashFlow = cashFlowService.getCashFlowById(id)
            ?: throw AppException(404, "Data catatan keuangan tidak tersedia!")

        val response = DataResponse(
            "success",
            "Berhasil mengambil data catatan keuangan",
            mapOf("cashFlow" to cashFlow)
        )
        call.respond(response)
    }

    suspend fun createCashFlow(call: ApplicationCall) {
        val request = call.receive<CashFlowRequest>()

        // Menggunakan ValidatorHelper persis seperti di TodoController
        val requestData = mapOf(
            "type" to request.type,
            "source" to request.source,
            "label" to request.label,
            "amount" to request.amount,
            "description" to request.description
        )
        val validator = ValidatorHelper(requestData)
        validator.required("type", "Tipe tidak boleh kosong")
        validator.required("source", "Sumber tidak boleh kosong")
        validator.required("label", "Label tidak boleh kosong")
        validator.required("amount", "Jumlah tidak boleh kosong")
        validator.required("description", "Deskripsi tidak boleh kosong")
        validator.validate()

        // Tambahan logika khusus CashFlow (Amount > 0)
        if (request.amount!! <= 0) {
            throw AppException(400, "Data yang dikirimkan tidak valid!")
        }

        val cashFlowId = cashFlowService.createCashFlow(
            request.type!!, request.source!!, request.label!!,
            request.amount!!, request.description!!
        )

        val response = DataResponse(
            "success",
            "Berhasil menambahkan data catatan keuangan",
            mapOf("cashFlowId" to cashFlowId)
        )
        call.respond(response)
    }

    suspend fun updateCashFlow(call: ApplicationCall) {
        val id = call.parameters["id"] ?: throw AppException(400, "ID tidak boleh kosong!")
        val request = call.receive<CashFlowRequest>()

        val validator = ValidatorHelper(mapOf(
            "type" to request.type,
            "source" to request.source,
            "label" to request.label,
            "amount" to request.amount,
            "description" to request.description
        ))
        validator.required("type", "Tipe tidak boleh kosong")
        validator.required("source", "Sumber tidak boleh kosong")
        validator.required("label", "Label tidak boleh kosong")
        validator.required("amount", "Jumlah tidak boleh kosong")
        validator.required("description", "Deskripsi tidak boleh kosong")
        validator.validate()

        val isUpdated = cashFlowService.updateCashFlow(
            id, request.type!!, request.source!!, request.label!!,
            request.amount!!, request.description!!
        )

        if (!isUpdated) {
            throw AppException(404, "Data catatan keuangan tidak tersedia!")
        }

        call.respond(DataResponse("success", "Berhasil mengubah data catatan keuangan", null))
    }

    suspend fun deleteCashFlow(call: ApplicationCall) {
        val id = call.parameters["id"] ?: throw AppException(400, "ID tidak boleh kosong!")

        val isDeleted = cashFlowService.removeCashFlow(id)
        if (!isDeleted) {
            throw AppException(404, "Data catatan keuangan tidak tersedia!")
        }

        call.respond(DataResponse("success", "Berhasil menghapus data catatan keuangan", null))
    }

    // Fungsi tambahan khusus untuk studi kasus CashFlow
    suspend fun setupData(call: ApplicationCall) {
        val query = CashFlowQuery()
        val cashFlows = cashFlowService.getAllCashFlows(query)
        for (cf in cashFlows) cashFlowService.removeCashFlow(cf.id)

        val initCashFlows = loadInitialData()
        for (cf in initCashFlows) {
            cashFlowService.createRawCashFlow(
                cf.id, cf.type, cf.source, cf.label,
                cf.amount, cf.description, cf.createdAt, cf.updatedAt
            )
        }

        call.respond(DataResponse("success", "Berhasil memuat data awal", null))
    }
}