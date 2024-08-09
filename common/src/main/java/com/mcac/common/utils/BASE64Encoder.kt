package com.mcac.common.utils

import java.util.*


class BASE64Encoder {
    fun encode(data: ByteArray?): String {
        return Base64.getEncoder().encodeToString(data)
    }
}