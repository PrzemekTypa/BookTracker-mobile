package com.example.booktrackermobile.repository

import com.example.booktrackermobile.model.Review
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

class ReviewRepository {

    private val db = FirebaseFirestore.getInstance()
    private val reviewsCollection = db.collection("reviews")

    // Dodawanie recenzji
    suspend fun addReview(review: Review): Boolean {
        return try {
            val data = hashMapOf(
                "bookKey" to review.bookKey,
                "userId" to review.userId,
                "rating" to review.rating,
                "reviewText" to review.reviewText,
                "timestamp" to FieldValue.serverTimestamp()
            )
            reviewsCollection.add(data).await()
            true
        } catch (e: Exception) {
            false
        }
    }


    // Pobieranie recenzji dla danej książki
    suspend fun getReviewsForBook(bookKey: String): List<Review> {
        val snapshot = reviewsCollection
            .whereEqualTo("bookKey", bookKey)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            val rating = (doc.getLong("rating") ?: 0).toInt()
            val reviewText = doc.getString("reviewText") ?: ""
            val userId = doc.getString("userId")
            val timestamp = doc.getTimestamp("timestamp")?.toDate()

            Review(
                bookKey = bookKey,
                userId = userId,
                rating = rating,
                reviewText = reviewText,
                timestamp = timestamp
            )
        }
    }
}
