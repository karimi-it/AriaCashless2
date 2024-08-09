package com.mcac.devices.ks8123.util

import java.util.*

class KeyObservable private constructor() : Observable() {

    private val subscribers = arrayListOf<Observer>()

    companion object {
        var instance: KeyObservable? = null

        internal fun getInstance(): KeyObservable {
            if (instance == null) {
                synchronized(this) {
                    instance = KeyObservable()
                }
            }
            return instance!!
        }
    }

    override fun addObserver(o: Observer?) {
        super.addObserver(o)
        subscribers.add(o!!)
    }

    override fun deleteObserver(o: Observer?) {
        super.deleteObserver(o)
        subscribers.remove(o!!)
    }

    override fun notifyObservers(arg: Any?) {
        for (subscriber in subscribers) {
            subscriber.update(this, arg)
        }
    }

    override fun deleteObservers() {
        super.deleteObservers()
        subscribers.clear()
    }
}