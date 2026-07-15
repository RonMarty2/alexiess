package com.aliexpressclone.app

import android.app.Application
import com.aliexpressclone.app.data.local.AppDatabase
import com.aliexpressclone.app.data.repository.CartRepository
import com.aliexpressclone.app.data.repository.OrderRepository
import com.aliexpressclone.app.data.repository.ProductRepository
import com.aliexpressclone.app.data.repository.ReviewRepository
import com.aliexpressclone.app.data.repository.SellerRepository
import com.aliexpressclone.app.data.repository.UserRepository
import com.aliexpressclone.app.data.seed.Seeder
import com.aliexpressclone.app.data.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AliExpressApp : Application() {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database by lazy { AppDatabase.getInstance(this) }

    val sessionManager by lazy { SessionManager(this) }

    val userRepository by lazy { UserRepository(database.userDao()) }
    val productRepository by lazy { ProductRepository(database.productDao(), database.categoryDao()) }
    val sellerRepository by lazy { SellerRepository(database.sellerDao()) }
    val reviewRepository by lazy { ReviewRepository(database.reviewDao()) }
    val cartRepository by lazy { CartRepository(database.cartDao()) }
    val orderRepository by lazy { OrderRepository(database.orderDao(), database.cartDao(), database.productDao()) }

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            Seeder(database).seedIfNeeded()
        }
    }
}
