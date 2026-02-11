package org.delcom.helpers

import kotlin.time.Instant

object CashFlowHelper {

    /**
     * Mengubah string tanggal format DD-MM-YYYY (dari JSON/HTTP)
     * menjadi objek Instant (untuk Entity)
     */
    fun parseToInstant(dateString: String): Instant {
        return try {
            // Asumsi format di data-awal.json adalah DD-MM-YYYY
            val parts = dateString.split("-")
            if (parts.size != 3) return Instant.fromEpochMilliseconds(0)

            // Mengubah ke format ISO (YYYY-MM-DD) agar bisa diparse Instant
            val isoString = "${parts[2]}-${parts[1]}-${parts[0]}T00:00:00Z"
            Instant.parse(isoString)
        } catch (e: Exception) {
            // Jika gagal, kembalikan waktu default (0)
            Instant.fromEpochMilliseconds(0)
        }
    }
}