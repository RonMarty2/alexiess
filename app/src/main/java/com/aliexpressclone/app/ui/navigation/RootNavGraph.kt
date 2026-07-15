package com.aliexpressclone.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.AliExpressApp
import com.aliexpressclone.app.data.local.entity.Role
import com.aliexpressclone.app.ui.admin.AdminRootScreen
import com.aliexpressclone.app.ui.buyer.BuyerRootScreen
import kotlinx.coroutines.launch

@Composable
fun RootNavGraph() {
    val app = LocalContext.current.applicationContext as AliExpressApp
    val userId by app.sessionManager.currentUserId.collectAsStateWithLifecycle(initialValue = null)
    val scope = rememberCoroutineScope()

    val currentUserId = userId
    if (currentUserId == null) {
        AuthNavGraph()
        return
    }

    var role by remember(currentUserId) { mutableStateOf<Role?>(null) }
    LaunchedEffect(currentUserId) {
        role = app.userRepository.getById(currentUserId)?.role
    }

    val resolvedRole = role
    if (resolvedRole == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val onLogout: () -> Unit = { scope.launch { app.sessionManager.logout() } }

    when (resolvedRole) {
        Role.ADMIN -> AdminRootScreen(userId = currentUserId, onLogout = onLogout)
        Role.BUYER -> BuyerRootScreen(userId = currentUserId, onLogout = onLogout)
    }
}
