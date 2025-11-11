package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.repositories

import android.util.Log
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models.*
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.services.CatalogService
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.Catalog
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.CatalogItem
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.SupplierInfo
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.repositories.CatalogRepository
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val apiService: CatalogService
) : CatalogRepository {

    override suspend fun getAllSuppliersWithCatalogs(): List<SupplierInfo> {
        val publishedCatalogs = apiService.getPublishedCatalogs().map { it.toDomainSafe() }
        val uniqueAccountIds = publishedCatalogs.map { it.ownerAccount }.distinct()

        return uniqueAccountIds.mapNotNull { accountId ->
            try {
                apiService.getAccountWithCatalogs(accountId).toDomain()
            } catch (e: Exception) {
                Log.e("CATALOG_REPO", "Error fetching supplier $accountId: ${e.message}")
                null
            }
        }
    }

    override suspend fun getSupplierById(accountId: String): SupplierInfo {
        return apiService.getAccountWithCatalogs(accountId).toDomain()
    }

    override suspend fun getPublishedCatalogs(): List<Catalog> {
        return apiService.getPublishedCatalogs().map { it.toDomainSafe() }
    }

    override suspend fun getAllCatalogs(): List<Catalog> {
        return apiService.getAllCatalogs().map { it.toDomainSafe() }
    }

    override suspend fun getCatalogById(catalogId: String): Catalog {
        return apiService.getCatalogById(catalogId).toDomainSafe()
    }

    override suspend fun createCatalog(
        accountId: String,
        name: String,
        description: String,
        contactEmail: String
    ): Catalog {
        val request = CreateCatalogRequest(name, description, contactEmail)
        val response = apiService.createCatalog(accountId, request)
        return response.toDomainSafe().also {
            Log.d("CATALOG_REPO", "Catalog created: ${it.id}, items: ${it.catalogItems.size}")
        }
    }

    override suspend fun updateCatalog(catalogId: String, name: String, description: String, contactEmail: String) {
        val request = UpdateCatalogRequest(name, description, contactEmail)
        apiService.updateCatalog(catalogId, request)
    }

    override suspend fun publishCatalog(catalogId: String) {
        apiService.publishCatalog(catalogId)
    }

    override suspend fun unpublishCatalog(catalogId: String) {
        apiService.unpublishCatalog(catalogId)
    }

    override suspend fun addCatalogItem(catalogId: String, productId: String, warehouseId: String, stock: Int): Catalog {
        val request = AddCatalogItemRequest(productId, warehouseId, stock)
        val response = apiService.addCatalogItem(catalogId, request)
        return response.toDomainSafe().also {
            Log.d("CATALOG_REPO", "Added item to catalog ${it.id}, total items: ${it.catalogItems.size}")
        }
    }

    override suspend fun removeCatalogItem(catalogId: String, productId: String) {
        apiService.removeCatalogItem(catalogId, productId)
    }

    override suspend fun getAllCatalogsByAccountId(accountId: String): List<Catalog> {
        return apiService.getCatalogsByAccountId(accountId).map { it.toDomainSafe() }
    }

    override suspend fun getCatalogItemById(catalogId: String, productId: String): CatalogItem? {
        val catalogDto = apiService.getCatalogById(catalogId)
        val catalog = catalogDto.toDomain()
        return catalog.catalogItems.find { it.productId == productId }
    }

}
fun CatalogDto.toDomainSafe(): Catalog {
    return Catalog(
        id = id,
        name = name,
        description = description,
        catalogItems = catalogItems?.map { it.toDomainSafe() } ?: emptyList(),
        ownerAccount = ownerAccount,
        contactEmail = contactEmail,
        isPublished = isPublished,
        warehouseId = warehouseId
    ).also { Log.d("CATALOG_MAPPER", "Mapped catalog: id=${it.id}, name=${it.name}, items=${it.catalogItems.size}") }
}

fun CatalogItemDto.toDomainSafe(): CatalogItem {
    return CatalogItem(
        productId = productId,
        productName = productName,
        unitPrice = unitPrice,
        imageUrl = imageUrl,
        availableStock = availableStock ?: 0
    )
}
