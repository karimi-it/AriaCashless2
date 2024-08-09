package com.mcac.devices.models


data class CardInfo(
    var SAK: Byte = 0,
    var ATQA0: Byte = 0,
    var ATQA1: Byte = 0,
    var uid: ByteArray? = byteArrayOf()
)
