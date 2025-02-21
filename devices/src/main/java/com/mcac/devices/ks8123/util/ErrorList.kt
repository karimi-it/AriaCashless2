package com.mcac.devices.ks8123.util

import com.mcac.devices.models.ErrorResponse


object ErrorList {
    private val errorMap = HashMap<Int,String>().apply {
        //General Error
        put(0,"Ok")
        put(-1,"FAILED")
        put(-100,"ERROR_PUBLIC")
        put(-101,"DEVICE_NO_PERMISSION")
        put(-102,"DEVICE_API_PARAM_ERROR")
        put(-103,"DEVICE_MALFUNCTION")
        put(-104,"DEVICE_PARAM_ERROR")
        put(-105,"DEVICE_NOT_SUPPORT")
        put(-106,"TIMEOUT")
        put(-107,"SYSTEM_ERROR")
        put(-108,"DEVICE_FORCE_CANCEL")
        put(-109,"DEVICE_FORCE_CLOSED")
        put(-110,"DEVICE_CONNECT_FAILS")
        put(-111,"DEVICE_NOT_OPEN")
        put(-112,"DEVICE_USED")
        put(-113,"DEVICE_OPENED")
        //Printer Error
        put(-200,"ERROR_PRINTER")
        put(-201,"PRINTER_NO_PAPER")
        put(-202,"PRINTER_JAM")
        put(-203,"PRINTER_HARDER")
        put(-204,"PRINTER_OVERHEAT")
        put(-205,"PRINTER_LOVELY")
        put(-206,"PRINTER_BUSY")
        put(-207,"PRINTER_LIFT_HEAD")
        put(-208,"PRINTER_CUT_POSITION_ERR")
        put(-209,"PRINTER_LOW_TEMP")
        put(-210,"PRINTER_UNKNOWN")
        //Pinpad Error
        put(-1000,"ERROR_PINPAD")
        put(-1001,"PINPAD_SELF_DESTRUCTION")
        put(-1002,"PINPAD_PWD_NULL")
        put(-1003,"PINPAD_KEY_NOT_EXIST")
        put(-1004,"PINPAD_KEY_USAGE_ERROR")
        put(-1005,"PINPAD_KEY_NOT_AUTHENTICATE")
        put(-1006,"PINPAD_USER_CANCEL")
        put(-1007,"PINPAD_USER_TIMEOUT")
        put(-1008,"PINPAD_KEY_PRESS")
        put(-1009,"PINPAD_TIMES_EXCEED")
        put(-2000,"ERROR_PINPAD_KEY")
        put(-2001,"PINPAD_KEY_MK_INDEX_ERR")
        put(-2002,"PINPAD_KEY_WK_INDEX_ERR")
        put(-2003,"PINPAD_KEY_BUFF_NULL_ERR")
        put(-2004,"PINPAD_KEY_LEN_ERR")
        put(-2005,"PINPAD_KEY_TYPE_ERR")
        put(-2006,"PINPAD_KEY_USAGE_ERR")
        put(-2007,"PINPAD_KEY_KVC_BUFF_NULL_ERR")
        put(-2008,"PINPAD_KEY_KVC_LEN_ERR")
        put(-2009,"PINPAD_KEY_MK_ENC_ERR")
        put(-2010,"PINPAD_KEY_KVC_DIFF_ERR")
        put(-2011,"PINPAD_KEY_MMK_READ_ERR")
        put(-2012,"PINPAD_KEY_ELRCK_READ_ERR")
        put(-2013,"PINPAD_KEY_LRC_ERR")
        put(-2014,"PINPAD_KEY_MODE_ERR")
        put(-2015,"PINPAD_KEY_DES_MODE_ERR")
        put(-2016,"PINPAD_KEY_DES_PIV_NULL_ERR")
        put(-2017,"PINPAD_KEY_DES_PSRC_LEN_ERR")
        put(-2018,"PINPAD_KEY_DES_PSRC_NULL_ERR")
        put(-2019,"PINPAD_KEY_DES_POUT_NULL_ERR")
        put(-2020,"PINPAD_KEY_DES_OUT_SIZE_ERR")
        put(-2021,"PINPAD_KEY_DES_OUT_LEN_ERR")
        put(-2022,"PINPAD_KEY_DES_TEXT_ERR")
        put(-2023,"PINPAD_KEY_DES_VOICE_ERR")
        put(-2024,"PINPAD_KEY_DES_PIN_IN_LEN_ERR")
        put(-2025,"PINPAD_KEY_DES_PIN_TYPE_ERR")
        put(-2026,"PINPAD_KEY_DES_OUT_LEN_NULL_ERR")
        put(-2027,"PINPAD_KEY_DES_NOT_INPUT_ERR")
        put(-2028,"PINPAD_KEY_DES_PIN_XOR_ERR")
        put(-2029,"PINPAD_KEY_DES_PIN_PAD_ERR")
        put(-2030,"PINPAD_KEY_DES_PIN_TIMEOUT_ERR")
        put(-2031,"PINPAD_KEY_DES_PIN_TITLE_ERR")
        put(-2032,"PINPAD_KEY_DES_PIN_FIR_PRO_ERR")
        put(-2033,"PINPAD_KEY_DES_PIN_SEC_PRO_ERR")
        put(-2034,"PINPAD_KEY_DES_PIN_AMOUNT_ERR")
        put(-2035,"PINPAD_KEY_DES_PIN_TITLE_LEN_ERR")
        put(-2036,"PINPAD_NVS_RAM_READ_ERR")
        put(-2037,"PINPAD_NVS_RAM_WRITE_ERR")
        put(-2038,"PINPAD_KEY_USED_INDEX_MAX_ERR")
        put(-2039,"PINPAD_KEY_USED_INDEX_READ_ERR")
        put(-2040,"PINPAD_KEY_CMP_SAME_ERR")
        put(-2041,"PINPAD_KEY_READ_ERR")
        put(-2043,"PINPAD_KEY_FILE_OPEN_ERR")
        put(-2044,"PINPAD_KEY_FILE_READ_LEN_ERR")
        put(-2045,"PINPAD_KEY_FILE_WRITE_LEN_ERR")
        put(-2046,"PINPAD_NVS_RAM_CRC_ERR")
        put(-2047,"PINPAD_NVS_RAM_INDEX_ERR")
        put(-2048,"PINPAD_NVS_RAM_BPK_FLAG_ERR")
        put(-2049,"PINPAD_DES_ECB_ERR")
        put(-2050,"PINPAD_ANTI_EXH_TYPE_ERR")
        put(-2051,"PINPAD_ANTI_EXH_CNT_ERR")
        put(-2052,"PINPAD_ANTI_EXH_MODE_ERR")
        put(-2053,"PINPAD_ANTI_EXH_PTR_NULL_ERR")
        put(-2054,"PINPAD_RESTRICT_KEY_ERR")
        put(-2055,"PINPAD_KEY_TYPE_INDEX_ERR")
        put(-2056,"PINPAD_ENC_DEC_MODE_ERR")
        put(-2057,"PINPAD_KEY_TYPE_ENC_INDEX_ERR")
        put(-2058,"PINPAD_VER_TYPE_ERR")
        put(-2059,"PINPAD_VER_BUFF_NULL_ERR")
        put(-2060,"PINPAD_VER_BUFF_SIZE_ERR")
        put(-2061,"PINPAD_PIN_OUT_LEN_INPUT_ERR")
        put(-2062,"PINPAD_DES_SM4_MODE_ERR")
        put(-2063,"PINPAD_DES_SM4_LEN_ERR")
        put(-2064,"PINPAD_DES_SM4_SIZE_ERR")
        put(-2065,"PINPAD_DES_SM4_PIN_ERR")
        put(-2066,"PINPAD_PIN_UART_CANCEL")
        put(-2067,"PINPAD_PIN_PLAY_TEXT_TYPE_ERR")
        put(-2068,"PINPAD_ENC_PIN_LEN_ERR")
        put(-2069,"PINPAD_KEY_WRITE_ERR")
        put(-2070,"PINPAD_NO_OLD_KEY_ERR")
        put(-2071,"PINPAD_DEFENDER_ERR")
        put(-2072,"PINPAD_PTK_ENC_LOAD_ERR")
        put(-2073,"PINPAD_RECOVER_KEY_ERR")
        put(-2074,"PINPAD_PTK_KEY_KCV_ERR")
        put(-2075,"PINPAD_SRC_DATA_LEN_ERR")
        //Card Error
        put(-1700,"ERROR_CARD")
        put(-1701,"CARD_TYPE_ERR")
        put(-1702,"CARD_SECTOR_AUTH_ERR")
        put(-1703,"CARD_SECTOR_NOT_AUTH")
        put(-1704,"CARD_REMOVED")
        put(-1705,"CARD_MALFUNCTION")
        put(-1706,"CARD_NOT_POWER")
        //Customer
        put(-1800,"CUSTOMER_DISPLAY")
        put(-1801,"CUSTOMER_NOT_FOUND")
        put(-1802,"CUSTOMER_COMM_ERROR")
        put(-1802,"CUSTOMER_SIZE_ERROR")
    }

    fun getError(code:Int): ErrorResponse {
        if (errorMap.containsKey(code)){
            return ErrorResponse(code, errorMap[code]!!)
        }
        else{
            return ErrorResponse(code, errorMap[-1]!!)
        }
    }

}