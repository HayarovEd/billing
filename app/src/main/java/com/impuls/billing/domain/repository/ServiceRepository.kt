package com.impuls.billing.domain.repository


interface ServiceRepository {
    fun getVersionName(): Double?
}