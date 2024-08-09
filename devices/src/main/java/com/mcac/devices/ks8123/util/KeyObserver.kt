package com.mcac.devices.ks8123.util

import com.mcac.devices.ks8123.util.KeyListener
import java.util.*

class KeyObserver constructor(val listener: KeyListener):Observer{

    override fun update(o: Observable?, arg: Any?) {
        listener.onKey(arg as Char)
    }
}