package com.mcac.common.utils

import com.mcac.common.models.BalanceResponse
import com.mcac.common.models.CardToCardResponse
import com.mcac.common.models.CardValidateResponse

fun printFormatGetBalance(response: BalanceResponse): String {
    val sb = StringBuilder()
    sb.append("موجودی حساب:").append("%,d".format(response.totalBalance.toBigInteger())).append("\n")
        .append("کد پیگیری:").append(response.stan.toBigInteger().toString()).append("\n")
        .append("شماره مرجع:").append(response.rrn).append("\n")
        .append("تاریخ تراکنش:").append(response.persianDate).append("\n")
        .append("ساعت تراکنش:").append(response.persianTime).append("\n")
    return sb.toString()
}

fun printFormatCardValidate(response: CardValidateResponse): String {
    val sb = StringBuilder()
    sb.append("نام صاحب حساب:").append("%35s".format(response.customerName)).append("\n")
        .append("کد پیگیری:").append("%36s".format(response.stan.toBigInteger().toString())).append("\n")
        .append("شماره مرجع:").append("%40s".format(response.rrn)).append("\n")
        .append("تاریخ تراکنش:").append("%35s".format(response.persianDate)).append("\n")
        .append("ساعت تراکنش:").append("%30s".format(response.persianTime)).append("\n")
    return sb.toString()
}

fun printFormatCardToCard(response: CardToCardResponse): String {
    val sb = StringBuilder()
        .append("کد پیگیری:").append("%10s".format(response.stan.toBigInteger().toString())).append("\n")
        .append("شماره مرجع:").append("%10s".format(response.rrn)).append("\n")
        .append("تاریخ تراکنش:").append("%10s".format(response.persianDate)).append("\n")
        .append("ساعت تراکنش:").append("%10s".format(response.persianTime)).append("\n")

    return sb.toString()
}