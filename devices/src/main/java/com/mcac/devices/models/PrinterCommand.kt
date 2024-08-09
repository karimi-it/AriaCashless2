package com.mcac.devices.models

import java.io.Serializable

class PrinterCommand(
    var text: String = "",
    var barCode: String = "",
    var qrCode: String = "",
    var image: String = "",
    var setting: SZZTPrinterSetting = SZZTPrinterSetting(),
) : Serializable