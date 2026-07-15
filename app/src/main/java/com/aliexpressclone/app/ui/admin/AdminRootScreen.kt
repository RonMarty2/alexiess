package com.aliexpressclone.app.ui.admin

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
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
import com.aliexpressclone.app.ui.admin.account.AdminAccountScreen
import com.aliexpressclone.app.ui.admin.dashboard.AdminDashboardScreen
import com.aliexpressclone.app.ui.admin.dashboard.AdminDashboardViewModel
import com.aliexpressclone.app.ui.admin.orders.AdminOrderDetailScreen
import com.aliexpressclone.app.ui.admin.orders.AdminOrderDetailViewModel
import com.aliexpressclone.app.ui.admin.orders.AdminOrdersScreen
import com.aliexpressclone.app.ui.admin.orders.AdminOrdersViewModel
import com.aliexpressclone.app.ui.admin.products.AdminProductEditScreen
import com.aliexpressclone.app.ui.admin.products.AdminProductEditViewModel
import com.aliexpressclone.app.ui.admin.products.AdminProductsScreen
import com.aliexpressclone.app.ui.admin.products.AdminProductsViewModel
import com.aliexpressclone.app.ui.admin.sellers.AdminSellerEditScreen
import com.aliexpressclone.app.ui.admin.sellers.AdminSellerEditViewModel
import com.aliexpressclone.app.ui.admin.sellers.AdminSellersScreen
import com.aliexpressclone.app.ui.admin.sellers.AdminSellersViewModel
import com.aliexpressclone.app.ui.buyer.account.AccountViewModel
import com.aliexpressclone.app.ui.common.ViewModelFactory
import com.aliexpressclone.app.ui.navigation.Routes
import com.aliexpressclone.app.ui.navigation.navigateToTab

private val bottomTabs = listOf(
    Routes.ADMIN_DASHBOARD to Pair(Icons.Filled.Dashboard, "Panel"),
    Routes.ADMIN_PRODUCTS to Pair(Icons.Filled.Inventory, "Productos"),
    Routes.ADMIN_SELLERS to Pair(Icons.Filled.Store, "Vendedores"),
    Routes.ADMIN_ORDERS to Pair(Icons.Filled.ListAlt, "Pedidos"),
    Routes.ADMIN_ACCOUNT to Pair(Icons.Filled.Person, "Cuenta")
)

@Composable
fun AdminRootScreen(userId: Long, onLogout: () -> Unit) {
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
            startDestination = Routes.ADMIN_DASHBOARD,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.ADMIN_DASHBOARD) {
                val vm: AdminDashboardViewModel = viewModel(factory = ViewModelFactory {
                    AdminDashboardViewModel(app.productRepository, app.orderRepository, app.userRepository)
                })
                AdminDashboardScreen(viewModel = vm)
            }
            composable(Routes.ADMIN_PRODUCTS) {
                val vm: AdminProductsViewModel = viewModel(factory = ViewModelFactory { AdminProductsViewModel(app.productRepository) })
                AdminProductsScreen(
                    viewModel = vm,
                    onAddProduct = { navController.navigate(Routes.adminProductEdit(0)) },
                    onEditProduct = { id -> navController.navigate(Routes.adminProductEdit(id)) }
                )
            }
            composable(
                route = Routes.ADMIN_PRODUCT_EDIT,
                arguments = listOf(navArgument("productId") { type = NavType.LongType })
            ) { entry ->
                val productId = entry.arguments?.getLong("productId") ?: 0L
                val vm: AdminProductEditViewModel = viewModel(factory = ViewModelFactory {
                    AdminProductEditViewModel(app.productRepository, app.sellerRepository, productId)
                })
                AdminProductEditScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onSaved = { navController.popBackStack() }
                )
            }
            composable(Routes.ADMIN_SELLERS) {
                val vm: AdminSellersViewModel = viewModel(factory = ViewModelFactory { AdminSellersViewModel(app.sellerRepository) })
                AdminSellersScreen(
                    viewModel = vm,
                    onAddSeller = { navController.navigate(Routes.adminSellerEdit(0)) },
                    onEditSeller = { id -> navController.navigate(Routes.adminSellerEdit(id)) }
                )
            }
            composable(
                route = Routes.ADMIN_SELLER_EDIT,
                arguments = listOf(navArgument("sellerId") { type = NavType.LongType })
            ) { entry ->
                val sellerId = entry.arguments?.getLong("sellerId") ?: 0L
                val vm: AdminSellerEditViewModel = viewModel(factory = ViewModelFactory {
                    AdminSellerEditViewModel(app.sellerRepository, sellerId)
                })
                AdminSellerEditScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onSaved = { navController.popBackStack() }
                )
            }
            composable(Routes.ADMIN_ORDERS) {
                val vm: AdminOrdersViewModel = viewModel(factory = ViewModelFactory {
                    AdminOrdersViewModel(app.orderRepository, app.userRepository)
                })
                AdminOrdersScreen(
                    viewModel = vm,
                    onOrderClick = { id -> navController.navigate(Routes.adminOrderDetail(id)) }
                )
            }
            composable(
                route = Routes.ADMIN_ORDER_DETAIL,
                arguments = listOf(navArgument("orderId") { type = NavType.LongType })
            ) { entry ->
                val orderId = entry.arguments?.getLong("orderId") ?: 0L
                val vm: AdminOrderDetailViewModel = viewModel(factory = ViewModelFactory {
                    AdminOrderDetailViewModel(app.orderRepository, orderId)
                })
                AdminOrderDetailScreen(viewModel = vm, onBack = { navController.popBackStack() })
            }
            composable(Routes.ADMIN_ACCOUNT) {
                val vm: AccountViewModel = viewModel(factory = ViewModelFactory { AccountViewModel(app.userRepository, userId) })
                AdminAccountScreen(viewModel = vm, onLogout = onLogout)
            }
        }
    }
}
