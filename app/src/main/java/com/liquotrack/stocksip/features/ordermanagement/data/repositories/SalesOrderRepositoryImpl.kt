package com.liquotrack.stocksip.features.ordermanagement.data.repositories

import com.liquotrack.stocksip.features.ordermanagement.data.remote.services.SalesOrderService
import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.CreateSalesOrderItemDto
import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.CreateSalesOrderRequestDto
import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.ProposeDeliveryScheduleRequestDto
import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.RespondDeliveryProposalRequestDto
import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.UpdateOrderStatusRequestDto
import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.SalesOrderDto
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderRepository
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderResponse
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderItemResource
import com.liquotrack.stocksip.features.ordermanagement.domain.DeliveryProposalResource
import com.liquotrack.stocksip.features.ordermanagement.domain.CreateOrderRequest
import com.liquotrack.stocksip.features.ordermanagement.domain.PurchaseOrderItem
import com.liquotrack.stocksip.features.ordermanagement.domain.OrderStatusResponse
import com.liquotrack.stocksip.features.ordermanagement.domain.SupplierOrdersResponse
import com.liquotrack.stocksip.features.ordermanagement.domain.LiquorStoreOwnerOrdersResponse
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SalesOrderRepositoryImpl @Inject constructor(private val service: SalesOrderService) : SalesOrderRepository {

    override suspend fun getOrderById(orderId: String): SalesOrderResponse = withContext(Dispatchers.IO) {
        val response = service.getOrderById(orderId)
        if (!response.isSuccessful) throw Exception("Error fetching order: ${response.code()} ${response.message()}")
        val dto = response.body() ?: throw Exception("Empty body")
        dto.toDomain()
    }

    override suspend fun getAllOrders(): List<SalesOrderResponse> = withContext(Dispatchers.IO) {
        val response = service.getAllOrders()
        if (!response.isSuccessful) throw Exception("Error fetching orders: ${response.code()} ${response.message()}")
        response.body()?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun createOrder(request: CreateOrderRequest): SalesOrderResponse = withContext(Dispatchers.IO) {
        val body = CreateSalesOrderRequestDto(
            orderCode = request.orderCode,
            purchaseOrderId = request.purchaseOrderId,
            items = request.items.map { it.toCreateItemDto() },
            status = request.status,
            catalogToBuyFrom = request.catalogToBuyFrom,
            receiptDate = request.receiptDate,
            completitionDate = request.completitionDate
        )
        val response = service.createOrder(body)
        if (!response.isSuccessful) throw Exception("Error creating order: ${response.code()} ${response.message()}")
        response.body()?.toDomain() ?: throw Exception("Empty body")
    }

    override suspend fun proposeDeliveryScheduleForOrder(orderId: String, proposedDate: String, notes: String?): SalesOrderResponse = withContext(Dispatchers.IO) {
        val response = service.proposeDeliveryScheduleForOrder(orderId, ProposeDeliveryScheduleRequestDto(proposedDate, notes))
        if (!response.isSuccessful) throw Exception("Error proposing delivery: ${response.code()} ${response.message()}")
        response.body()?.toDomain() ?: throw Exception("Empty body")
    }

    override suspend fun getDeliveryProposalForOrder(orderId: String): SalesOrderResponse = withContext(Dispatchers.IO) {
        val response = service.getDeliveryProposalForOrder(orderId)
        if (!response.isSuccessful) throw Exception("Error getting proposal: ${response.code()} ${response.message()}")
        response.body()?.toDomain() ?: throw Exception("Empty body")
    }

    override suspend fun respondToDeliveryProposal(orderId: String, accept: Boolean, notes: String?): SalesOrderResponse = withContext(Dispatchers.IO) {
        val response = service.respondToDeliveryProposal(orderId, RespondDeliveryProposalRequestDto(accept, notes))
        if (!response.isSuccessful) throw Exception("Error responding proposal: ${response.code()} ${response.message()}")
        response.body()?.toDomain() ?: throw Exception("Empty body")
    }

    override suspend fun getDeliveryProposalAndResponse(orderId: String): SalesOrderResponse = withContext(Dispatchers.IO) {
        val response = service.getDeliveryProposalAndResponse(orderId)
        if (!response.isSuccessful) throw Exception("Error getting proposal & response: ${response.code()} ${response.message()}")
        response.body()?.toDomain() ?: throw Exception("Empty body")
    }

    override suspend fun updateOrderStatus(orderId: String, newStatusAlias: String, reason: String?): SalesOrderResponse = withContext(Dispatchers.IO) {
        val normalized = newStatusAlias.trim().uppercase()
        val defaultNewStatus = when (normalized) {
            "CONFIRM" -> "Confirmed"
            "CANCEL" -> "Canceled"
            else -> "Processing" // PENDING
        }
        val body = UpdateOrderStatusRequestDto(
            newStatus = defaultNewStatus,
            newStatusAlias = normalized,
            reason = reason
        )
        val response = service.updateOrderStatus(orderId, body)
        if (!response.isSuccessful) throw Exception("Error updating status: ${response.code()} ${response.message()}")
        response.body()?.toDomain() ?: throw Exception("Empty body")
    }

    override suspend fun getOrderStatus(orderId: String): OrderStatusResponse = withContext(Dispatchers.IO) {
        val response = service.getOrderStatus(orderId)
        if (!response.isSuccessful) throw Exception("Error getting status: ${response.code()} ${response.message()}")
        response.body()?.let { dto ->
            OrderStatusResponse(
                orderId = dto.orderId,
                status = dto.status,
                message = dto.message,
                lastUpdated = dto.lastUpdated,
                details = dto.details
            )
        } ?: throw Exception("Empty body")
    }

    override suspend fun getOrdersBySupplierId(supplierId: String): SupplierOrdersResponse = withContext(Dispatchers.IO) {
        val response = service.getOrdersBySupplierId(supplierId)
        if (!response.isSuccessful) throw Exception("Error getting supplier orders: ${response.code()} ${response.message()}")
        response.body()?.let { dto ->
            SupplierOrdersResponse(
                supplierId = dto.supplierId,
                orders = dto.orders.map { it.toDomain() },
                totalOrders = dto.totalOrders,
                message = dto.message
            )
        } ?: throw Exception("Empty body")
    }

    override suspend fun getOrdersByLiquorStoreOwnerId(liquorStoreOwnerId: String): LiquorStoreOwnerOrdersResponse = withContext(Dispatchers.IO) {
        val response = service.getOrdersByLiquorStoreOwnerId(liquorStoreOwnerId)
        if (!response.isSuccessful) throw Exception("Error getting LSO orders: ${response.code()} ${response.message()}")
        response.body()?.let { dto ->
            LiquorStoreOwnerOrdersResponse(
                liquorStoreOwnerId = dto.liquorStoreOwnerId,
                orders = dto.orders.map { it.toDomain() },
                totalOrders = dto.totalOrders,
                message = dto.message
            )
        } ?: throw Exception("Empty body")
    }

    private fun SalesOrderDto.toDomain(): SalesOrderResponse = SalesOrderResponse(
        id = id,
        orderCode = orderCode,
        purchaseOrderId = purchaseOrderId,
        items = items.map { SalesOrderItemResource(it.productId, it.unitPrice, it.currency, it.inventoryId, it.quantityToSell) },
        status = status,
        catalogToBuyFrom = catalogToBuyFrom,
        receiptDate = receiptDate,
        completitionDate = completitionDate,
        buyer = buyer,
        deliveryProposal = deliveryProposal?.let { DeliveryProposalResource(it.proposedDate, it.notes, it.status, it.createdAt, it.respondedAt) }
    )

    private fun PurchaseOrderItem.toCreateItemDto(): CreateSalesOrderItemDto =
        CreateSalesOrderItemDto(
            productId = productId,
            unitPrice = unitPrice,
            currency = currency,
            inventoryId = inventoryId,
            quantityToSell = quantityToSell
        )
}