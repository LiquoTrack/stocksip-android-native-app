package com.liquotrack.stocksip.features.ordermanagement.data.repositories

import com.liquotrack.stocksip.features.ordermanagement.data.remote.services.SalesOrderService
import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.SalesOrderDto
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderRepository
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderResponse
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderItemResource
import com.liquotrack.stocksip.features.ordermanagement.domain.DeliveryProposalResource
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SalesOrderRepositoryImpl @Inject constructor(
    private val service: SalesOrderService
) : SalesOrderRepository {

    override suspend fun createSalesOrderFromPurchaseOrder(purchaseOrderId: String): SalesOrderResponse =
        withContext(Dispatchers.IO) {
            val response = service.createSalesOrderFromPurchaseOrder(purchaseOrderId)
            if (!response.isSuccessful) throw Exception("Error creating sales order: ${response.code()} ${response.message()}")
            val dto = response.body() ?: throw Exception("Empty body")
            dto.toDomain()
        }

    override suspend fun getOrderById(orderId: String): SalesOrderResponse = withContext(Dispatchers.IO) {
        val response = service.getOrderById(orderId)
        if (!response.isSuccessful) throw Exception("Error fetching order: ${response.code()} ${response.message()}")
        val dto = response.body() ?: throw Exception("Empty body")
        dto.toDomain()
    }

    private fun SalesOrderDto.toDomain(): SalesOrderResponse = SalesOrderResponse(
        id = id,
        orderCode = orderCode,
        purchaseOrderId = purchaseOrderId,
        items = items.map {
            SalesOrderItemResource(
                productId = it.productId,
                unitPrice = it.unitPrice,
                currency = it.currency,
                inventoryId = it.inventoryId,
                quantityToSell = it.quantityToSell
            )
        },
        status = status,
        catalogToBuyFrom = catalogToBuyFrom,
        receiptDate = receiptDate,
        completitionDate = completitionDate,
        buyer = buyer,
        deliveryProposal = deliveryProposal?.let {
            DeliveryProposalResource(it.proposedDate, it.notes, it.status, it.createdAt, it.respondedAt)
        }
    )
}