package pl.androidcoder.mockk

import android.os.BatteryManager
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.util.*

class BatteryStatusViewModelTest {

    private val batteryManager = mockk<BatteryManager>()
    private val batteryStatusViewModel = BatteryStatusViewModel(batteryManager)

    @Test
    fun name() {
        Locale.setDefault(Locale.US)
        assertBatteryEnergy(12345678, "12.35 mW\\h")
        Locale.setDefault(Locale.GERMANY)
        assertBatteryEnergy(12345678, "12,35 mW\\h")
    }

    private fun assertBatteryEnergy(energy: Long, expectedText: String) {
        every { batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER) } returns energy
        assertThat(batteryStatusViewModel.getBatteryEnergy(), equalTo(expectedText))
    }
}
