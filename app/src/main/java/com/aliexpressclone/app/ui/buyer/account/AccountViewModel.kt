package com.aliexpressclone.app.ui.buyer.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.User
import com.aliexpressclone.app.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AccountViewModel(
    userRepository: UserRepository,
    userId: Long
) : ViewModel() {

    val user: StateFlow<User?> = userRepository.observeById(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
