package com.mcac.devices.models

import java.io.Serializable

data class SingPadCommand(
    var htmlData: String = "",
    var pdfData: ByteArray? = null
) : Serializable