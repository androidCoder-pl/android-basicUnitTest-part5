package pl.androidcoder.mockk

import io.mockk.clearStaticMockk
import io.mockk.every
import io.mockk.mockkStatic
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

class TimeTest {
    @Before
    fun setUp() {
        mockkStatic(Calendar::class)
    }

    @After
    fun tearDown() {
        clearStaticMockk(Calendar::class)
    }

    @Test
    fun name() {
        val calendar = GregorianCalendar(2013, 1, 28, 0, 0, 1)
        every { Calendar.getInstance() } returns calendar
        assertThat(Time.getMillisFromMidnight(), equalTo(1))

        val calendar1 = GregorianCalendar(2013, 1, 28, 0, 3, 1)
        every { Calendar.getInstance() } returns calendar1
        assertThat(Time.getMillisFromMidnight(), equalTo(3*60 + 1))

        val calendar2 = GregorianCalendar(2013, 1, 28, 2, 3, 1)
        every { Calendar.getInstance() } returns calendar2
        assertThat(Time.getMillisFromMidnight(), equalTo(2*60*60 + 3 * 60 + 1))
    }
}