package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.repositories

import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models.AddCatalogItemRequest
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models.CreateCatalogRequest
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models.UpdateCatalogRequest
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models.toDomain
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.services.CatalogService
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.Catalog
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.SupplierInfo
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.repositories.CatalogRepository
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val apiService: CatalogService
) : CatalogRepository {

    override suspend fun getAllSuppliersWithCatalogs(): List<SupplierInfo> {
        val publishedCatalogs = apiService.getPublishedCatalogs()
            .map { it.toDomain() }

        val uniqueAccountIds = publishedCatalogs
            .map { it.ownerAccount }
            .distinct()

        return uniqueAccountIds.mapNotNull { accountId ->
            try {
                apiService.getAccountWithCatalogs(accountId).toDomain()
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun getSupplierById(accountId: String): SupplierInfo {
        return apiService.getAccountWithCatalogs(accountId).toDomain()
    }

    override suspend fun getPublishedCatalogs(): List<Catalog> {
        return apiService.getPublishedCatalogs().map { it.toDomain() }
    }

    override suspend fun getAllCatalogs(): List<Catalog> {
        return apiService.getAllCatalogs().map { it.toDomain() }
    }

    override suspend fun getCatalogById(catalogId: String): Catalog {
        return apiService.getCatalogById(catalogId).toDomain()
    }

    override suspend fun createCatalog(
        accountId: String,
        name: String,
        description: String,
        contactEmail: String
    ): Catalog {
        val request = CreateCatalogRequest(name, description, contactEmail)
        return apiService.createCatalog(accountId, request).toDomain()
    }

    override suspend fun updateCatalog(
        catalogId: String,
        name: String,
        description: String,
        contactEmail: String
    ) {
        val request = UpdateCatalogRequest(name, description, contactEmail)
        apiService.updateCatalog(catalogId, request)
    }

    override suspend fun publishCatalog(catalogId: String) {
        apiService.publishCatalog(catalogId)
    }

    override suspend fun unpublishCatalog(catalogId: String) {
        apiService.unpublishCatalog(catalogId)
    }

    override suspend fun addCatalogItem(
        catalogId: String,
        productId: String,
        warehouseId: String,
        stock: Int
    ): Catalog {
        val request = AddCatalogItemRequest(productId, warehouseId, stock)
        return apiService.addCatalogItem(catalogId, request).toDomain()
    }

    override suspend fun removeCatalogItem(catalogId: String, productId: String) {
        apiService.removeCatalogItem(catalogId, productId)
    }

    override suspend fun getAllCatalogsByAccountId(accountId: String): List<Catalog> {
        val response = apiService.getCatalogsByAccountId(accountId)
        return response.map { it.toDomain() }
    }
}