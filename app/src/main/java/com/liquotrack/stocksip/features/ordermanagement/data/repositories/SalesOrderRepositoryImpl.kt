package com.liquotrack.stocksip.features.ordermanagement.data.repositories

import com.liquotrack.stocksip.features.ordermanagement.data.remote.services.SalesOrderService
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SalesOrderRepositoryImpl @Inject constructor(private val service: SalesOrderService) : SalesOrderRepository {
    override suspend fun generatePurchaseOrder(orderCode: String): String {
        return withContext(Dispatchers.IO) {
            try {
                service.generatePurchaseOrder()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun createSalesOrderFromCompletedProcurementOrder(): String {
        return withContext(Dispatchers.IO) {
            try {
                service.createSalesOrderFromCompletedProcurementOrder()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun getOrderById(orderCode: String): String {
        return withContext(Dispatchers.IO) {
            try {
                service.getOrderById(orderCode)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun getAllOrders(): String {
        return withContext(Dispatchers.IO) {
            try {
                service.getAllOrders()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun createOrder(): String {
        return withContext(Dispatchers.IO) {
            try {
                service.createOrder()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun proposeDeliveryScheduleForOrder(): String {
        return withContext(Dispatchers.IO) {
            throw UnsupportedOperationException("orderId is required to propose a delivery schedule but is not provided by the repository interface")
        }
    }

    override suspend fun getDeliveryProposalForOrder(): String {
        return withContext(Dispatchers.IO) {
            throw UnsupportedOperationException("orderId is required to get the delivery proposal but is not provided by the repository interface")
        }
    }

    override suspend fun respondToDeliveryProposal(): String {
        return withContext(Dispatchers.IO) {
            throw UnsupportedOperationException("orderId is required to respond to a delivery proposal but is not provided by the repository interface")
        }
    }

    override suspend fun getDeliveryProposalAndResponse(): String {
        return withContext(Dispatchers.IO) {
            throw UnsupportedOperationException("orderId is required to get proposal and response but is not provided by the repository interface")
        }
    }

    override suspend fun updateOrderStatus(): String {
        return withContext(Dispatchers.IO) {
            throw UnsupportedOperationException("orderId is required to update order status but is not provided by the repository interface")
        }
    }

    override suspend fun getOrderStatus(): String {
        return withContext(Dispatchers.IO) {
            throw UnsupportedOperationException("orderId is required to get order status but is not provided by the repository interface")
        }
    }

    override suspend fun getOrdersBySupplierId(): String {
        return withContext(Dispatchers.IO) {
            throw UnsupportedOperationException("supplierId is required to get orders by supplier but is not provided by the repository interface")
        }
    }

    override suspend fun getOrdersByLiquorStoreOwnerId(): String {
        return withContext(Dispatchers.IO) {
            throw UnsupportedOperationException("liquorStoreOwnerId is required to get orders by liquor store owner but is not provided by the repository interface")
        }
    }
}