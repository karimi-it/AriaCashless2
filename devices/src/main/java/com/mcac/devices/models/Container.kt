package com.mcac.devices.models

import com.mcac.devices.enums.CommandType
import com.mcac.devices.enums.DeviceType
import com.mcac.devices.enums.Status
import java.io.Serializable

data class Container(
    var deviceType: DeviceType = DeviceType.NONE,
    var commandType: CommandType = CommandType.NONE,
    var status: Status = Status.NONE,
    var obj: Any? = null
):Serializable