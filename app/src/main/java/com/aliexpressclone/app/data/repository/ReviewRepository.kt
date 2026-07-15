package com.aliexpressclone.app.data.repository

import com.aliexpressclone.app.data.local.dao.ReviewDao
import com.aliexpressclone.app.data.local.entity.Review
import kotlinx.coroutines.flow.Flow

class ReviewRepository(private val reviewDao: ReviewDao) {

    fun observeForProduct(productId: Long): Flow<List<Review>> = reviewDao.observeForProduct(productId)

    fun observeByUser(userId: Long): Flow<List<Review>> = reviewDao.observeByUser(userId)

    suspend fun submitReview(productId: Long, userId: Long, buyerName: String, rating: Float, comment: String) {
        val existing = reviewDao.getByProductAndUser(productId, userId)
        val review = Review(
            id = existing?.id ?: 0,
            productId = productId,
            userId = userId,
            buyerName = buyerName,
            rating = rating,
            comment = comment,
            createdAt = System.currentTimeMillis()
        )
        if (existing != null) {
            reviewDao.update(review)
        } else {
            reviewDao.insert(review)
        }
    }
}
