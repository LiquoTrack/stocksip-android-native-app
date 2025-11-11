package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models

import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.AccountInfo
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.BusinessInfo
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.Catalog
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.CatalogItem
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.SupplierInfo

fun SupplierInfoDto.toDomain(): SupplierInfo {
    return SupplierInfo(
        account = account.toDomain(),
        catalogs = catalogs.map { it.toDomain() }
    )
}

fun AccountInfoDto.toDomain(): AccountInfo {
    return AccountInfo(
        id = id,
        business = business.toDomain()
    )
}

fun BusinessInfoDto.toDomain(): BusinessInfo {
    return BusinessInfo(
        businessName = businessName,
        businessEmail = businessEmail,
        ruc = ruc
    )
}

fun CatalogDto.toDomain(): Catalog {
    return Catalog(
        id = id,
        name = name,
        description = description,
        catalogItems = catalogItems.map { it.toDomain() },
        ownerAccount = ownerAccount,
        contactEmail = contactEmail,
        isPublished = isPublished,
        warehouseId = warehouseId,
    )
}

fun CatalogItemDto.toDomain(): CatalogItem {
    return CatalogItem(
        productId = productId,
        productName = productName,
        unitPrice = unitPrice,
        imageUrl = imageUrl,
        availableStock = availableStock
    )
}