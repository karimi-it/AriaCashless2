package com.mcac.devices.ks8123

import com.mcac.devices.enums.CommandType
import com.mcac.devices.enums.DeviceType
import com.mcac.devices.enums.Status
import com.mcac.devices.hardwares.IMagneticCardReader
import com.mcac.devices.ks8123.util.ArrayUtil
import com.mcac.devices.ks8123.util.ErrorList
import com.mcac.devices.ks8123.util.StatusList
import com.mcac.devices.models.*
import com.szzt.sdk.device.Constants
import com.szzt.sdk.device.card.MagneticStripeCardReader
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MagneticCardReaderImpl private constructor(private val magnetCardReader: MagneticStripeCardReader) :
    IMagneticCardReader {


    companion object {
        var Instance: MagneticCardReaderImpl? = null

        fun getInstance(magnetCardReader: MagneticStripeCardReader): MagneticCardReaderImpl {
            if (Instance == null) {
                synchronized(this) {
                    Instance = MagneticCardReaderImpl(magnetCardReader)
                }
            }
            return Instance!!
        }
    }

    override suspend fun open(): Container {
        val result = magnetCardReader.open()
        if (result >= Constants.Error.OK) {
            return Container(DeviceType.MAGNET_CARD, CommandType.OPEN, Status.SUCCESS)
        }
        else{
            return Container(DeviceType.MAGNET_CARD,CommandType.OPEN,Status.FAIL)
        }
    }

    override suspend fun setting(): Container {
        TODO("Not yet implemented")
    }

    override suspend fun readTracks(command: MagnetCardCommand): Container = suspendCoroutine{
        magnetCardReader.setCheckLrc(1)
        val tracks: Array<String?> =  arrayOfNulls(3)
        val result = magnetCardReader.waitForCard(command.timeOut)
        if (result >= Constants.Error.OK) {
            val checkFlag: ByteArray =
                magnetCardReader.getTrackData(MagneticStripeCardReader.TRACK_CHECK_FLAG)
            it.resume(Container(DeviceType.MAGNET_CARD,CommandType.MAGNET_CARD_READ_TRACKS,Status.SUCCESS,MagnetCardResponse().apply {
                if (checkFlag[0].toInt() != 0){
                    val data = magnetCardReader.getTrackData(MagneticStripeCardReader.TRACK_INDEX_1)
                    if (data != null)
                        track1 = String(ArrayUtil.trackTrim(data))
                }
                if (checkFlag[1].toInt() != 0){
                    val data = magnetCardReader.getTrackData(MagneticStripeCardReader.TRACK_INDEX_2)
                    if (data != null)
                        track2 = String(ArrayUtil.trackTrim(data))
                }
                if (checkFlag[2].toInt() != 0){
                    val data = magnetCardReader.getTrackData(MagneticStripeCardReader.TRACK_INDEX_3)
                    if (data != null)
                        track3 = String(ArrayUtil.trackTrim(data))
                }
            }))
        }
        else{
            it.resume(Container(DeviceType.MAGNET_CARD,CommandType.MAGNET_CARD_READ_TRACKS,Status.FAIL,
                ErrorList.getError(result)))
        }

    }

    override suspend fun listenerForCard(command: MagnetCardCommand): Container = suspendCoroutine {
        try{
            magnetCardReader.listenForCard(command.timeOut
            ) { result ->
                if (result >= Constants.Error.OK) {
                    it.resume(
                        Container(
                            DeviceType.MAGNET_CARD,
                            CommandType.MAGNET_CARD_LISTENER_FOR_CARD,
                            Status.SUCCESS,
                            MagnetCardResponse().apply {
                                val checkFlag: ByteArray =
                                    magnetCardReader.getTrackData(MagneticStripeCardReader.TRACK_CHECK_FLAG)
                                if (checkFlag[0].toInt() != 0){
                                    val data = magnetCardReader.getTrackData(MagneticStripeCardReader.TRACK_INDEX_1)
                                    if (data != null)
                                        track1 = ArrayUtil.trackFormat(ArrayUtil.trackTrim(data))
                                }
                                if (checkFlag[1].toInt() != 0){
                                    val data = magnetCardReader.getTrackData(MagneticStripeCardReader.TRACK_INDEX_2)
                                    if (data != null)
                                        track2 = ArrayUtil.trackFormat(ArrayUtil.trackTrim(data))
                                }
                                if (checkFlag[2].toInt() != 0){
                                    val data = magnetCardReader.getTrackData(MagneticStripeCardReader.TRACK_INDEX_3)
                                    if (data != null)
                                        track3 = ArrayUtil.trackFormat(ArrayUtil.trackTrim(data))
                                }

                            })
                    )
                } else {
                    it.resume(
                        Container(
                            DeviceType.MAGNET_CARD,
                            CommandType.MAGNET_CARD_LISTENER_FOR_CARD,
                            Status.FAIL,
                            ErrorList.getError(result)
                        )
                    )
                }
            }
        }catch (exception:Exception){
            it.resume(
                Container(
                    DeviceType.MAGNET_CARD,
                    CommandType.MAGNET_CARD_LISTENER_FOR_CARD,
                    Status.EXCEPTION,
                    exception.message?.let { item -> ErrorResponse(-1, item) }
                )
            )
        }

    }

    override suspend fun status(): Container {
        val result = magnetCardReader.status
        return Container(
            DeviceType.MAGNET_CARD,
            CommandType.STATUS,
            Status.SUCCESS,
            StatusList.getPublicStatus(result)
        )
    }

    override suspend fun reset(): Container {
        if (destroy().status == Status.SUCCESS && open().status == Status.SUCCESS) {
            return Container(DeviceType.MAGNET_CARD, CommandType.RESET, Status.SUCCESS)
        }
        return Container(DeviceType.MAGNET_CARD, CommandType.RESET, Status.FAIL)
    }

    override suspend fun destroy(): Container {
        var result = magnetCardReader.close()
        return if (result >= Constants.Error.OK) {
            Container(DeviceType.MAGNET_CARD, CommandType.CLOSE, Status.SUCCESS)
        } else {
            Container(
                DeviceType.MAGNET_CARD,
                CommandType.CLOSE,
                Status.FAIL,
                ErrorList.getError(result)
            )
        }
    }

    override fun isSupported(): Boolean {
        return true
    }
}