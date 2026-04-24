package com.impuls.billing.domain.utils

const val APP_PREFERENCES = "auth_settings"
const val BASE_URL = /*"http://10.222.222.174:9998/api/"*/ "http://77.236.64.205:9998/api/"
const val BASE_PAYMENT_URL = "https://pfapi.ckassa.ru/api-shop/rs/payform/"
const val NUMBER_NPO_CODIT = "111-16915-2"
const val X_GWT_PERMUTATION_VALUE = "DA5774A738084094C8AC1B6AD1844285"
const val X_GWT_PERMUTATION = "x-gwt-permutation"
const val X_GWT_MODULE_BASE_VALUE = "https://ckassa.ru/payment/payment/"
const val X_GWT_MODULE_BASE = "x-gwt-module-base"
const val CONTENT_TYPE = "Content-Type"
const val CONTENT_TYPE_X_GWT = "text/x-gwt-rpc; charset=UTF-8"
const val BODY_CKASSA_INIT_SERVICE = "7|1|6|$X_GWT_MODULE_BASE_VALUE|$X_GWT_PERMUTATION_VALUE|_|searchProviderByCode|8m|$NUMBER_NPO_CODIT|1|2|3|4|1|5|6|"


const val LAST_EMAIL = "last_email"
const val LAST_PASSWORD = "last_password"
const val LABEL_BEARER = "label_bearer"