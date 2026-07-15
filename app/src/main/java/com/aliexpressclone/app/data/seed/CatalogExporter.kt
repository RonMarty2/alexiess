package com.aliexpressclone.app.data.seed

import android.net.Uri
import android.util.Base64
import com.aliexpressclone.app.data.repository.ProductRepository
import com.aliexpressclone.app.data.repository.SellerRepository
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * Serializes the current catalog (sellers, categories, products and their real
 * photos) into a single JSON file, so an admin can hand it over to be baked
 * back into the app's permanent seed data.
 */
object CatalogExporter {

    suspend fun buildJson(productRepository: ProductRepository, sellerRepository: SellerRepository): String {
        val sellers = sellerRepository.getAll()
        val categories = productRepository.getCategories()
        val products = productRepository.getAllProducts()
        val sellerNameById = sellers.associate { it.id to it.name }
        val categoryNameById = categories.associate { it.id to it.name }

        val root = JSONObject()

        val sellersArray = JSONArray()
        sellers.forEach { seller ->
            val obj = JSONObject()
            obj.put("name", seller.name)
            obj.put("description", seller.description)
            obj.put("logoBase64", encodeLocalImage(seller.logoUri) ?: JSONObject.NULL)
            sellersArray.put(obj)
        }
        root.put("sellers", sellersArray)

        val categoriesArray = JSONArray()
        categories.forEach { category ->
            val obj = JSONObject()
            obj.put("name", category.name)
            obj.put("emoji", category.emoji)
            obj.put("colorHex", category.colorHex)
            categoriesArray.put(obj)
        }
        root.put("categories", categoriesArray)

        val productsArray = JSONArray()
        products.forEach { product ->
            val obj = JSONObject()
            obj.put("name", product.name)
            obj.put("description", product.description)
            obj.put("price", product.price)
            obj.put("originalPrice", product.originalPrice)
            obj.put("stock", product.stock)
            obj.put("rating", product.rating)
            obj.put("soldCount", product.soldCount)
            obj.put("freeShipping", product.freeShipping)
            obj.put("placeholderEmoji", product.placeholderEmoji)
            obj.put("placeholderColorHex", product.placeholderColorHex)
            obj.put("sellerName", product.sellerId?.let { sellerNameById[it] } ?: JSONObject.NULL)
            obj.put("categoryName", product.categoryId?.let { categoryNameById[it] } ?: JSONObject.NULL)
            obj.put("imageBase64", encodeLocalImage(product.imageUri) ?: JSONObject.NULL)
            productsArray.put(obj)
        }
        root.put("products", productsArray)

        return root.toString(2)
    }

    private fun encodeLocalImage(uriString: String?): String? {
        if (uriString.isNullOrBlank()) return null
        return try {
            val path = Uri.parse(uriString).path ?: return null
            val bytes = File(path).readBytes()
            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }
}
