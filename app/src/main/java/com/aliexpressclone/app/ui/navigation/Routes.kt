package com.aliexpressclone.app.ui.navigation

import android.net.Uri

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Buyer bottom-nav tabs
    const val BUYER_HOME = "buyer_home"
    const val BUYER_CATEGORIES = "buyer_categories"
    const val BUYER_CART = "buyer_cart"
    const val BUYER_ACCOUNT = "buyer_account"

    // Buyer detail screens
    const val BUYER_SEARCH = "buyer_search"

    const val BUYER_CATEGORY_PRODUCTS = "buyer_category/{categoryId}/{categoryName}"
    fun buyerCategoryProducts(id: Long, name: String) = "buyer_category/$id/${Uri.encode(name)}"

    const val BUYER_PRODUCT_DETAIL = "buyer_product/{productId}"
    fun buyerProductDetail(id: Long) = "buyer_product/$id"

    const val BUYER_STORE = "buyer_store/{storeName}"
    fun buyerStore(name: String) = "buyer_store/${Uri.encode(name)}"

    const val BUYER_CHECKOUT = "buyer_checkout"
    const val BUYER_ORDERS = "buyer_orders?status={status}"
    fun buyerOrders(status: String? = null) = "buyer_orders?status=${status ?: "ALL"}"

    const val BUYER_ORDER_DETAIL = "buyer_order_detail/{orderId}"
    fun buyerOrderDetail(id: Long) = "buyer_order_detail/$id"

    // Admin bottom-nav tabs
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val ADMIN_PRODUCTS = "admin_products"
    const val ADMIN_ORDERS = "admin_orders"
    const val ADMIN_ACCOUNT = "admin_account"

    const val ADMIN_PRODUCT_EDIT = "admin_product_edit/{productId}"
    fun adminProductEdit(id: Long) = "admin_product_edit/$id"

    const val ADMIN_ORDER_DETAIL = "admin_order_detail/{orderId}"
    fun adminOrderDetail(id: Long) = "admin_order_detail/$id"
}
