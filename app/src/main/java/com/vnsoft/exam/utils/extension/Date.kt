package com.vnsoft.exam.utils.extension

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun Date.timeAgo(): String {
    val SECOND_MILLIS: Int = 1000
    val MINUTE_MILLIS: Int = 60 * SECOND_MILLIS
    val HOUR_MILLIS: Int = 60 * MINUTE_MILLIS
    val DAY_MILLIS: Int = 24 * HOUR_MILLIS

    var time = getTime();
    if (time < 1000000000000L) {
        time *= 1000
    }
    val now = System.currentTimeMillis()
    if (time > now || time <= 0) {
        return ""
    }
    val diff = now - time
    return if (diff < MINUTE_MILLIS) {
        "Vừa xong"
    } else if (diff < 2 * MINUTE_MILLIS) {
        "1 phút trước"
    } else if (diff < 50 * MINUTE_MILLIS) {
        (diff / MINUTE_MILLIS).toString() + " phút trước"
    } else if (diff < 90 * MINUTE_MILLIS) {
        "1 giờ trước"
    } else if (diff < 24 * HOUR_MILLIS) {
        (diff / HOUR_MILLIS).toString() + " giờ trước"
    } else if (diff < 48 * HOUR_MILLIS) {
        "Hôm qua"
    } else {
        (diff / DAY_MILLIS).toString() + " ngày trước"
    }
}

fun String.formatDate( format: String): String? {
    @SuppressLint("SimpleDateFormat") val formatterInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    var date: Date? = null
    try {
        date = formatterInput.parse(toString())
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    @SuppressLint("SimpleDateFormat") val formatter = SimpleDateFormat(format)
    return if (date == null) {
        toString()
    } else formatter.format(date)
}

fun String.convertDate(): Date? {
    @SuppressLint("SimpleDateFormat") val formatterInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    try {
        return formatterInput.parse(toString())
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}
@SuppressLint("SimpleDateFormat")
fun Long.convertDate(): String? {
    val df = SimpleDateFormat("dd/MM/yyyy")
    return df.format(toLong())
}
