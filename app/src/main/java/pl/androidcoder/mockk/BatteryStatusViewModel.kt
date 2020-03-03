package pl.androidcoder.mockk

import android.os.BatteryManager
import java.math.RoundingMode
import java.text.DecimalFormat

class BatteryStatusViewModel(
    private val battery: BatteryManager
) {

    companion object {
        private const val NANO_TO_MILLIS = 0.000_001 // 10^-9
    }

    fun getBatteryEnergy(): String {
        val energy = battery.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER) * NANO_TO_MILLIS
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_DOWN
        return "${df.format(energy)} mW\\h"
    }
}