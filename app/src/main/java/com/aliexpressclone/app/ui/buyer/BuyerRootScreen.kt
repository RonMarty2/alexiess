package com.aliexpressclone.app.ui.buyer

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aliexpressclone.app.AliExpressApp
import com.aliexpressclone.app.data.local.entity.OrderStatus
import com.aliexpressclone.app.ui.buyer.account.AccountScreen
import com.aliexpressclone.app.ui.buyer.account.AccountViewModel
import com.aliexpressclone.app.ui.buyer.cart.CartScreen
import com.aliexpressclone.app.ui.buyer.cart.CartViewModel
import com.aliexpressclone.app.ui.buyer.categories.CategoriesScreen
import com.aliexpressclone.app.ui.buyer.checkout.CheckoutScreen
import com.aliexpressclone.app.ui.buyer.checkout.CheckoutViewModel
import com.aliexpressclone.app.ui.buyer.home.HomeScreen
import com.aliexpressclone.app.ui.buyer.home.HomeViewModel
import com.aliexpressclone.app.ui.buyer.orders.OrderDetailScreen
import com.aliexpressclone.app.ui.buyer.orders.OrderDetailViewModel
import com.aliexpressclone.app.ui.buyer.orders.OrdersScreen
import com.aliexpressclone.app.ui.buyer.orders.OrdersViewModel
import com.aliexpressclone.app.ui.buyer.product.ProductDetailScreen
import com.aliexpressclone.app.ui.buyer.product.ProductDetailViewModel
import com.aliexpressclone.app.ui.buyer.search.SearchScreen
import com.aliexpressclone.app.ui.buyer.search.SearchViewModel
import com.aliexpressclone.app.ui.buyer.store.StoreScreen
import com.aliexpressclone.app.ui.buyer.store.StoreViewModel
import com.aliexpressclone.app.ui.common.ViewModelFactory
import com.aliexpressclone.app.ui.navigation.Routes
import com.aliexpressclone.app.ui.navigation.navigateToTab

private val bottomTabs = listOf(
    Routes.BUYER_HOME to Pair(Icons.Filled.Home, "Inicio"),
    Routes.BUYER_CATEGORIES to Pair(Icons.Filled.Apps, "Categorías"),
    Routes.BUYER_CART to Pair(Icons.Filled.ShoppingCart, "Cesta"),
    Routes.BUYER_ACCOUNT to Pair(Icons.Filled.Person, "Cuenta")
)

@Composable
fun BuyerRootScreen(userId: Long, onLogout: () -> Unit) {
    val app = LocalContext.current.applicationContext as AliExpressApp
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (bottomTabs.any { it.first == currentRoute }) {
                NavigationBar {
                    bottomTabs.forEach { (route, iconLabel) ->
                        val (icon, label) = iconLabel
                        NavigationBarItem(
                            selected = currentRoute == route,
                            onClick = { navController.navigateToTab(route) },
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.BUYER_HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.BUYER_HOME) {
                val vm: HomeViewModel = viewModel(factory = ViewModelFactory { HomeViewModel(app.productRepository) })
                HomeScreen(
                    viewModel = vm,
                    onSearchClick = { navController.navigate(Routes.BUYER_SEARCH) },
                    onCategoryClick = { category -> navController.navigate(Routes.buyerCategoryProducts(category.id, category.name)) },
                    onProductClick = { id -> navController.navigate(Routes.buyerProductDetail(id)) }
                )
            }
            composable(Routes.BUYER_CATEGORIES) {
                val vm: HomeViewModel = viewModel(factory = ViewModelFactory { HomeViewModel(app.productRepository) })
                CategoriesScreen(
                    viewModel = vm,
                    onCategoryClick = { category -> navController.navigate(Routes.buyerCategoryProducts(category.id, category.name)) }
                )
            }
            composable(Routes.BUYER_SEARCH) {
                val vm: SearchViewModel = viewModel(factory = ViewModelFactory { SearchViewModel(app.productRepository) })
                SearchScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onProductClick = { id -> navController.navigate(Routes.buyerProductDetail(id)) }
                )
            }
            composable(
                route = Routes.BUYER_CATEGORY_PRODUCTS,
                arguments = listOf(
                    navArgument("categoryId") { type = NavType.LongType },
                    navArgument("categoryName") { type = NavType.StringType }
                )
            ) { entry ->
                val categoryId = entry.arguments?.getLong("categoryId") ?: 0L
                val categoryName = entry.arguments?.getString("categoryName") ?: ""
                val vm: SearchViewModel = viewModel(factory = ViewModelFactory {
                    SearchViewModel(app.productRepository, categoryId = categoryId, categoryName = categoryName)
                })
                SearchScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onProductClick = { id -> navController.navigate(Routes.buyerProductDetail(id)) }
                )
            }
            composable(
                route = Routes.BUYER_PRODUCT_DETAIL,
                arguments = listOf(navArgument("productId") { type = NavType.LongType })
            ) { entry ->
                val productId = entry.arguments?.getLong("productId") ?: 0L
                val vm: ProductDetailViewModel = viewModel(factory = ViewModelFactory {
                    ProductDetailViewModel(app.productRepository, app.cartRepository, app.reviewRepository, userId, productId)
                })
                ProductDetailScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onStoreClick = { name -> navController.navigate(Routes.buyerStore(name)) },
                    onBuyNow = { navController.navigate(Routes.BUYER_CHECKOUT) },
                    onProductClick = { id -> navController.navigate(Routes.buyerProductDetail(id)) }
                )
            }
            composable(
                route = Routes.BUYER_STORE,
                arguments = listOf(navArgument("storeName") { type = NavType.StringType })
            ) { entry ->
                val storeName = entry.arguments?.getString("storeName") ?: ""
                val vm: StoreViewModel = viewModel(factory = ViewModelFactory { StoreViewModel(app.productRepository, storeName) })
                StoreScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onProductClick = { id -> navController.navigate(Routes.buyerProductDetail(id)) }
                )
            }
            composable(Routes.BUYER_CART) {
                val vm: CartViewModel = viewModel(factory = ViewModelFactory { CartViewModel(app.cartRepository, userId) })
                CartScreen(
                    viewModel = vm,
                    onCheckout = { navController.navigate(Routes.BUYER_CHECKOUT) }
                )
            }
            composable(Routes.BUYER_CHECKOUT) {
                val vm: CheckoutViewModel = viewModel(factory = ViewModelFactory {
                    CheckoutViewModel(app.cartRepository, app.orderRepository, app.userRepository, userId)
                })
                CheckoutScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onOrderPlaced = { orderId ->
                        navController.navigate(Routes.buyerOrderDetail(orderId)) {
                            popUpTo(Routes.BUYER_HOME)
                        }
                    }
                )
            }
            composable(
                route = Routes.BUYER_ORDERS,
                arguments = listOf(navArgument("status") { type = NavType.StringType; defaultValue = "ALL" })
            ) { entry ->
                val statusArg = entry.arguments?.getString("status")
                val initialStatus = statusArg?.takeIf { it != "ALL" }?.let { runCatching { OrderStatus.valueOf(it) }.getOrNull() }
                val vm: OrdersViewModel = viewModel(factory = ViewModelFactory { OrdersViewModel(app.orderRepository, userId, initialStatus) })
                OrdersScreen(
                    viewModel = vm,
                    onOrderClick = { id -> navController.navigate(Routes.buyerOrderDetail(id)) }
                )
            }
            composable(
                route = Routes.BUYER_ORDER_DETAIL,
                arguments = listOf(navArgument("orderId") { type = NavType.LongType })
            ) { entry ->
                val orderId = entry.arguments?.getLong("orderId") ?: 0L
                val vm: OrderDetailViewModel = viewModel(factory = ViewModelFactory {
                    OrderDetailViewModel(app.orderRepository, app.reviewRepository, app.userRepository, userId, orderId)
                })
                OrderDetailScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onProductClick = { id -> navController.navigate(Routes.buyerProductDetail(id)) }
                )
            }
            composable(Routes.BUYER_ACCOUNT) {
                val vm: AccountViewModel = viewModel(factory = ViewModelFactory { AccountViewModel(app.userRepository, userId) })
                AccountScreen(
                    viewModel = vm,
                    onOrdersClick = { status -> navController.navigate(Routes.buyerOrders(status?.name)) },
                    onLogout = onLogout
                )
            }
        }
    }
}
