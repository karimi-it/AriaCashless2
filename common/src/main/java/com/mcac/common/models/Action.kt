package com.mcac.common.models

import com.mcac.common.enums.ActionType

data class Action(
    var code:Int = 0,
    var message:String = "",
    var messagePrint:String = "",
    var distId:Int = 0,
    var delay:Long = 0,
    var actionType: ActionType = ActionType.NONE,
    var actionIconId:Int = 0
):java.io.Serializable