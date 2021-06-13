package io.github.takusan23.designbridge.tool

import java.text.SimpleDateFormat

object TimeFormatter {

    /**
     * UnixTimeをフォーマットして返す
     * @param date UnixTime。Javaの場合はミリ秒
     * @return yyyy年MM月dd日 HH時mm分ss秒
     * */
    fun unixTimeMsToFormatText(date: Long): String {
        return SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒").format(date)
    }

}