package com.ian.grithaul.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ian.grithaul.data.local.entities.UserEntity
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Register a new resident
    suspend fun registerUser(
        fullName: String,
        email: String,
        phone: String,
        zone: String,
        password: String
    ): Result<UserEntity> {
        return try {
            // Create Firebase Auth account
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("User ID not found")

            // Create user profile in Firestore
            val user = UserEntity(
                id = uid,
                fullName = fullName,
                email = email,
                phone = phone,
                zone = zone,
                role = "resident"
            )

            firestore.collection("users")
                .document(uid)
                .set(user)
                .await()

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Login existing user
    suspend fun loginUser(
        email: String,
        password: String
    ): Result<UserEntity> {
        return try {
            // Sign in with Firebase Auth
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("User ID not found")

            // Fetch user profile from Firestore
            val document = firestore.collection("users")
                .document(uid)
                .get()
                .await()

            val user = document.toObject(UserEntity::class.java)
                ?: throw Exception("User profile not found")

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get current logged in user
    suspend fun getCurrentUser(): Result<UserEntity> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("No user logged in")

            val document = firestore.collection("users")
                .document(uid)
                .get()
                .await()

            val user = document.toObject(UserEntity::class.java)
                ?: throw Exception("User profile not found")

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Logout
    fun logoutUser() {
        auth.signOut()
    }

    // Check if user is logged in
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Get current user role
    suspend fun getCurrentUserRole(): String {
        return try {
            val uid = auth.currentUser?.uid ?: return "none"
            val document = firestore.collection("users")
                .document(uid)
                .get()
                .await()
            document.getString("role") ?: "none"
        } catch (e: Exception) {
            "none"
        }
    }
}
