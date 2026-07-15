package com.aliexpressclone.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aliexpressclone.app.data.local.dao.CartDao
import com.aliexpressclone.app.data.local.dao.CategoryDao
import com.aliexpressclone.app.data.local.dao.OrderDao
import com.aliexpressclone.app.data.local.dao.ProductDao
import com.aliexpressclone.app.data.local.dao.ReviewDao
import com.aliexpressclone.app.data.local.dao.SellerDao
import com.aliexpressclone.app.data.local.dao.UserDao
import com.aliexpressclone.app.data.local.entity.CartItem
import com.aliexpressclone.app.data.local.entity.Category
import com.aliexpressclone.app.data.local.entity.Order
import com.aliexpressclone.app.data.local.entity.OrderItem
import com.aliexpressclone.app.data.local.entity.OrderTrackingEvent
import com.aliexpressclone.app.data.local.entity.Product
import com.aliexpressclone.app.data.local.entity.Review
import com.aliexpressclone.app.data.local.entity.Seller
import com.aliexpressclone.app.data.local.entity.User

@Database(
    entities = [
        User::class,
        Category::class,
        Product::class,
        Seller::class,
        CartItem::class,
        Order::class,
        OrderItem::class,
        OrderTrackingEvent::class,
        Review::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun sellerDao(): SellerDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aliexpress_clone.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}
