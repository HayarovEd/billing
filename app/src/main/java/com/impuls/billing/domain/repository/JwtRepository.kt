package com.impuls.billing.domain.repository

import com.impuls.billing.domain.model.UserData

interface JwtRepository {
    fun decodeJwt(token: String): UserData?
}