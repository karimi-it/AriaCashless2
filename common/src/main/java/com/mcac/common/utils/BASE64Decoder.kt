package com.mcac.common.utils

import java.util.*

class BASE64Decoder {
    fun decodeBuffer(text: String?): ByteArray {
        return Base64.getDecoder().decode(text)
    }
}