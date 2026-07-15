package com.aliexpressclone.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aliexpressclone.app.AliExpressApp
import com.aliexpressclone.app.ui.auth.AuthViewModel
import com.aliexpressclone.app.ui.auth.LoginScreen
import com.aliexpressclone.app.ui.auth.RegisterScreen
import com.aliexpressclone.app.ui.common.ViewModelFactory

@Composable
fun AuthNavGraph() {
    val app = LocalContext.current.applicationContext as AliExpressApp
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            val vm: AuthViewModel = viewModel(factory = ViewModelFactory { AuthViewModel(app.userRepository, app.sessionManager) })
            LoginScreen(
                viewModel = vm,
                onLoginSuccess = { /* session flow drives navigation to buyer/admin root */ },
                onGoToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }
        composable(Routes.REGISTER) {
            val vm: AuthViewModel = viewModel(factory = ViewModelFactory { AuthViewModel(app.userRepository, app.sessionManager) })
            RegisterScreen(
                viewModel = vm,
                onRegisterSuccess = { /* session flow drives navigation to buyer/admin root */ },
                onBackToLogin = { navController.popBackStack() }
            )
        }
    }
}
