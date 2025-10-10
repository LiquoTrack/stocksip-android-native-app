package com.liquotrack.stocksip.features.careguides.domain

interface CareGuideRepository {

    /**
     * Gets all care guides by account ID.
     *
     * @param accountId The ID of the account to retrieve care guides for.
     * @return A list of care guides associated with the account.
     */
    suspend fun getById(accountId: String): List<CareGuide>
    /**
     * Gets all care guides by care guide ID.
     *
     * @param careGuideId The ID of the care guide to retrieve.
     * @return The care guide associated with the ID.
     */
    suspend fun getAllCareGuideBytId(careGuideId: String): CareGuide
    /**
     * Creates a new care guide.
     *
     * @param careGuide The care guide to create.
     * @return The created care guide.
     */
    suspend fun createCareGuide(careGuide: CareGuide): CareGuide
    /**
     * Updates an existing care guide.
     *
     * @param careGuide The care guide to update.
     * @return The updated care guide.
     */
    suspend fun updateCareGuide(careGuide: CareGuide): CareGuide
    /**
     * Deletes a care guide by ID.
     *
     * @param careGuideId The ID of the care guide to delete.
     */
    suspend fun deleteCareGuide(careGuideId: String)
}