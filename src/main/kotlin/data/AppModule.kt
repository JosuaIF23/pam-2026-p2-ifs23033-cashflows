package org.delcom.data

import org.delcom.controllers.CashFlowController
import org.delcom.repositories.ICashFlowRepository
import org.delcom.repositories.CashFlowRepository
import org.delcom.services.ICashFlowService
import org.delcom.services.CashFlowService
import org.koin.dsl.module

val cashflowModule = module {
    // Repository
    single<ICashFlowRepository> {
        CashFlowRepository()
    }

    // Service
    single<ICashFlowService> {
        CashFlowService(get())
    }

    // Controller
    single {
        CashFlowController(get())
    }
}