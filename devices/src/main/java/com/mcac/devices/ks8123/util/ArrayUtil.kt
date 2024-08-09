package com.mcac.devices.ks8123.util

object ArrayUtil {
    fun trackTrim(data: ByteArray): ByteArray {
            var end = 0
            for (i in data.indices.reversed()) {
                if (i != 0) {
                    end = i
                    break
                }
            }
            val d = ByteArray(end + 1)
            System.arraycopy(data, 0, d, 0, end + 1)
            return d
    }

    fun trackFormat(data: ByteArray): String {
        return if (data == null) {
            "no track info"
        } else {
            String(trackTrim(data))
        }
    }
}