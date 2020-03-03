package pl.androidcoder.mockk

import android.app.Activity
import android.content.Context
import android.os.BatteryManager
import androidx.core.content.edit
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*


class ActivityConfigurator(
    private val activity: Activity
) {
    fun configure() {
        activity.title = "Hello world"
        activity.actionBar?.setDisplayShowHomeEnabled(true)
        activity.actionBar?.setHomeButtonEnabled(true)
    }
}