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

    override suspend fun createSalesOrderFromPurchaseOrder(purchaseOrderId: String, catalogIdBuyFrom: String?): SalesOrderResponse =
        withContext(Dispatchers.IO) {
            println(">>> [Repo] POST /orders/from-procurement/$purchaseOrderId")
            if (catalogIdBuyFrom != null) {
                println(">>> [Repo] Catalog ID: $catalogIdBuyFrom")
            }

            val response = service.createSalesOrderFromPurchaseOrder(purchaseOrderId, catalogIdBuyFrom)

            if (!response.isSuccessful) {
                println(">>> [Repo] Error: ${response.code()} - ${response.message()}")
                throw Exception("Error creating sales order: ${response.code()} ${response.message()}")
            }

            val dto = response.body() ?: throw Exception("Empty body")

            println(">>> [Repo] SalesOrder: ${dto.id}")
            dto.toDomain()
        }

    override suspend fun getAllOrders(): List<SalesOrderResponse> =
        withContext(Dispatchers.IO) {

            println(">>> [Repo] GET /orders")

            val response = service.getAllOrders()

            if (!response.isSuccessful) {
                throw Exception("Error fetching all orders: ${response.code()} ${response.message()}")
            }

            val dtoList = response.body() ?: emptyList()

            dtoList.map { it.toDomain() }
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
            DeliveryProposalResource(
                it.proposedDate,
                it.notes,
                it.status,
                it.createdAt,
                it.respondedAt
            )
        },
        supplierId = supplierId
    )
}
