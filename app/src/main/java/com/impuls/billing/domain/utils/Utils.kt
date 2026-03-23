package com.impuls.billing.domain.utils


fun findInXmlString(
    tag: String,
    xml: String
): String? {
    val pattern = "<$tag>(.*?)</$tag>".toRegex(RegexOption.DOT_MATCHES_ALL)
    return pattern.find(xml)?.groupValues?.get(1)
}