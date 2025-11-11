package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.services

import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models.AddCatalogItemRequest
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models.CatalogDto
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models.CreateCatalogRequest
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models.SupplierInfoDto
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models.UpdateCatalogRequest
import retrofit2.Response
import retrofit2.http.*

interface CatalogService {
    @GET("accounts/{accountId}/catalogs")
    suspend fun getCatalogsByAccountId(
        @Path("accountId") accountId: String
    ): List<CatalogDto>

    @GET("catalogs/with-business")
    suspend fun getAccountWithCatalogs(
        @Query("accountId") accountId: String
    ): SupplierInfoDto

    @GET("catalogs/published")
    suspend fun getPublishedCatalogs(): List<CatalogDto>

    @GET("catalogs")
    suspend fun getAllCatalogs(): List<CatalogDto>

    @GET("catalogs/{catalogId}")
    suspend fun getCatalogById(
        @Path("catalogId") catalogId: String
    ): CatalogDto

    @POST("accounts/{accountId}/catalogs")
    suspend fun createCatalog(
        @Path("accountId") accountId: String,
        @Body request: CreateCatalogRequest
    ): CatalogDto

    @PUT("catalogs/{catalogId}")
    suspend fun updateCatalog(
        @Path("catalogId") catalogId: String,
        @Body request: UpdateCatalogRequest
    ): Response<Unit>

    @PUT("catalogs/{catalogId}/publications")
    suspend fun publishCatalog(
        @Path("catalogId") catalogId: String
    ): Response<Unit>

    @DELETE("catalogs/{catalogId}/publications")
    suspend fun unpublishCatalog(
        @Path("catalogId") catalogId: String
    ): Response<Unit>

    @POST("catalogs/{catalogId}/items")
    suspend fun addCatalogItem(
        @Path("catalogId") catalogId: String,
        @Body request: AddCatalogItemRequest
    ): CatalogDto

    @DELETE("catalogs/{catalogId}/items/{productId}")
    suspend fun removeCatalogItem(
        @Path("catalogId") catalogId: String,
        @Path("productId") productId: String
    ): Response<Unit>
}