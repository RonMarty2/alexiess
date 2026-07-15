package com.aliexpressclone.app.data.local

import androidx.room.TypeConverter
import com.aliexpressclone.app.data.local.entity.OrderStatus
import com.aliexpressclone.app.data.local.entity.Role

class Converters {
    @TypeConverter
    fun fromRole(role: Role): String = role.name

    @TypeConverter
    fun toRole(value: String): Role = Role.valueOf(value)

    @TypeConverter
    fun fromOrderStatus(status: OrderStatus): String = status.name

    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus = OrderStatus.valueOf(value)
}
