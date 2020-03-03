package pl.androidcoder.mockk

import java.util.*

object Time {
    fun getMillisFromMidnight(): Int {
        val calendar = Calendar.getInstance() //static mock
        val seconds = calendar.get(Calendar.SECOND)
        val minutes = calendar.get(Calendar.MINUTE) * 60
        val hours = calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60
        return seconds + minutes + hours
    }
}