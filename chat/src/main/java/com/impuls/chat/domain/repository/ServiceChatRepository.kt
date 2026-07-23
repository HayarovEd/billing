package com.impuls.chat.domain.repository

import com.impuls.chat.domain.model.DeviceData

interface ServiceChatRepository {
    fun getDeviceData(): DeviceData
}