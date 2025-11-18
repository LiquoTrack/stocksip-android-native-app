package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.repositories

import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.Catalog
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.CatalogItem
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.SupplierInfo

interface CatalogRepository {

    suspend fun getAllSuppliersWithCatalogs(): List<SupplierInfo>

    suspend fun getSupplierById(accountId: String): SupplierInfo

    suspend fun getPublishedCatalogs(): List<Catalog>

    suspend fun getAllCatalogs(): List<Catalog>

    suspend fun getCatalogById(catalogId: String): Catalog

    suspend fun createCatalog(
        accountId: String,
        name: String,
        description: String,
        contactEmail: String
    ): Catalog

    suspend fun updateCatalog(
        catalogId: String,
        name: String,
        description: String,
        contactEmail: String
    )

    suspend fun publishCatalog(catalogId: String)

    suspend fun unpublishCatalog(catalogId: String)

    suspend fun addCatalogItem(
        catalogId: String,
        productId: String,
        warehouseId: String,
        stock: Int
    ): Catalog

    suspend fun removeCatalogItem(catalogId: String, productId: String)

    suspend fun getAllCatalogsByAccountId(accountId: String): List<Catalog>

    suspend fun getCatalogItemById(catalogId: String, productId: String): CatalogItem?
}