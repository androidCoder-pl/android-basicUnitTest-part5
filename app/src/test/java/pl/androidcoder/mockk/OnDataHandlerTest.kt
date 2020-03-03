package pl.androidcoder.mockk

import io.mockk.*
import org.junit.Test

class OnDataHandlerTest {
    @Test
    fun `handler should save data in singleton`() {
        mockkStatic(SomeSingleton::class) {
            val mockedSingleton = mockk<SomeSingleton>(relaxed = true)
            every { SomeSingleton.getSingleton() } returns mockedSingleton

            OnDataHandler().onNewData("Some data")

            verify { mockedSingleton.data = "Some data" }
        }
    }
}