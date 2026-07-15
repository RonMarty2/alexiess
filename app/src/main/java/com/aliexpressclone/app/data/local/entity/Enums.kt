package com.aliexpressclone.app.data.local.entity

enum class Role {
    ADMIN,
    BUYER
}

enum class OrderStatus(val label: String) {
    A_PAGAR("A pagar"),
    PROCESANDO("Procesando"),
    ENVIADO("Enviado"),
    EN_TRANSITO("En tránsito"),
    FINALIZADO("Finalizado"),
    CANCELADO("Cancelado")
}
