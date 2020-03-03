package pl.androidcoder.mockk

import android.app.Activity
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class ActivityConfiguratorTest {
    private val activity = mockk<Activity>(relaxed = true)

    @Test
    fun testActivityConfiguration() {
        //GIVEN
        val configurator = ActivityConfigurator(activity)
        //WHEN
        configurator.configure()
        //THEN
        verify { activity.title = "Hello world" }
        verify { activity.actionBar?.setDisplayShowHomeEnabled(true) }
        verify { activity.actionBar?.setHomeButtonEnabled(true) }
    }
}