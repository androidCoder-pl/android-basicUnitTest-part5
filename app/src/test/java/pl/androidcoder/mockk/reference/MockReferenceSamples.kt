package pl.androidcoder.mockk.reference


import io.mockk.*
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.contains
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Assert.fail
import org.junit.Test
import kotlin.reflect.KClass

class MockReferenceSamples {

    @After
    fun tearDown() {
        unmockkAll()
    }


    //Mockowanie zwracanych wartości klasy

    @Test
    fun `test mock returned values`() {
        val realData = mockk<RealData> {
            every { id } returns "mockData"
            every { name } returns "mockName"
            every { intNum } returns 600
            every { doubleNum } returns 600.0
        }

        //mock ustawia wartości ustawione za pomocą metody every
        assertThat(realData.id, equalTo("mockData"))
        assertThat(realData.name, equalTo("mockName"))
        assertThat(realData.intNum, equalTo(600))
        assertThat(realData.doubleNum, equalTo(600.0))
    }

    //Mock z wartościami domyślnymi

    @Test(expected = MockKException::class)
    fun `test mockk`() {
        val realData = mockk<RealData>()
        realData.id
    }

    @Test
    fun `test relaxed mockk`() {
        val realData = mockk<RealData>(relaxed = true)

        //mock ustawia puste ciągi znaków jako domyślne
        assertThat(realData.id, equalTo(""))
        assertThat(realData.name, equalTo(""))
        //mock ustawia dla liczb wartości równe 0
        assertThat(realData.intNum, equalTo(0))
        assertThat(realData.doubleNum, equalTo(0.0))
    }

    @Test(expected = MockKException::class)
    fun `test relaxed unit fun mockk get value`() {
        val realData = mockk<RealData>(relaxUnitFun = true)
        realData.id
    }

    @Test
    fun `test relaxed unit fun mockk`() {
        val realData = mockk<RealData>(relaxUnitFun = true)
        realData.reverseName()
    }

    //Mockowanie zachowania metody

    @Test
    fun `test mock answers`() {
        val realData = mockk<RealData>(relaxed = true) {
            every { reverseName() } answers { println("Text reversed!") }
        }

        realData.reverseName()
    }

    interface RealDataReposiotry {
        fun getAsync(callback: (data: RealData) -> Unit)
    }

    @Test
    fun `test async func`() {
        val givenRealData = RealData()
        val repository = mockk<RealDataReposiotry> {
            every { getAsync(any()) } answers {
                val callback = it.invocation.args[0] as (data: RealData) -> Unit
                callback.invoke(givenRealData)
            }
        }

        repository.getAsync {
            assertThat(it, equalTo(givenRealData))
        }
    }


    //Mockowanie z dopasowaniem argumentów

    @Test
    fun `test mock with argument matching`() {
        val expectedRealData = RealData("1")
        val anyExpectedRealData = RealData("any")
        val manager = mockk<Manager> {
            every { findRealData(any()) } returns anyExpectedRealData
            every { findRealData("1") } returns expectedRealData
            every { findRealData("2") } returns null
            every { findRealData("exception") } throws IllegalAccessError()
            every { findRealData("exception") } throws IllegalAccessError()
            every { findRealData(match { it.contains("my") }) } returns expectedRealData
        }

        assertThat(manager.findRealData("_____"), equalTo(anyExpectedRealData))
        assertThat(manager.findRealData("1"), equalTo(expectedRealData))
        assertThat(manager.findRealData("2"), nullValue())
        assertFail(IllegalAccessError::class) {
            manager.findRealData("exception")
        }
        assertThat(manager.findRealData("myId"), equalTo(expectedRealData))

    }

    @Test
    fun `test mock with partial argument matching`() {
        val anyIdExpectedRealData = RealData("1", "name1")
        val anyNameExpectedRealData = RealData("1", "anyName")
        val anyExpectedRealData = RealData("any", "anyName")

        val manager = mockk<Manager> {
            every { findRealData(any(), any()) } returns anyExpectedRealData
            every { findRealData("1", any()) } returns anyNameExpectedRealData
            every { findRealData(any(), "name1") } returns anyIdExpectedRealData
            every { findRealData(any(), isNull()) } throws IllegalAccessError()
        }

        assertThat(manager.findRealData("_____", "____"), equalTo(anyExpectedRealData))
        assertThat(manager.findRealData("1", "_____"), equalTo(anyNameExpectedRealData))
        assertThat(manager.findRealData("____", "name1"), equalTo(anyIdExpectedRealData))
        assertFail(IllegalAccessError::class) {
            manager.findRealData("exception", null)
        }
    }

    //Jeden mock wiele interfejsów

    @Test
    fun `test combine two interfaces into one mock`() {
        val interfaces = mockk<FirstInterface>(
            relaxed = true, moreInterfaces = *arrayOf(
                SecondInterface::class
            )
        )

        interfaces.firstAction()
        (interfaces as SecondInterface).secondAction()

        verify { interfaces.firstAction() }
        verify { interfaces.secondAction() }
    }

    //Mockowanie statycznych i globalnych metod

    @Test
    fun `test mocking java static method`() {
        mockkStatic(Utils::class)
        every { Utils.getTime() } returns 1

        assertThat(Utils.getTime(), equalTo(1L))
    }

    @Test
    fun `test mocking global method`() {
        mockkStatic("pl.androidcoder.mockk.reference.UtilsGlobalFunKt")
        every { getTime() } returns 1

        assertThat(getTime(), equalTo(1L))
    }

    //Mockowanie obiektów i companion object

    @Test
    fun `test mocking companion object`() {
        mockkObject(UtilsKotlin.Companion)
        every { UtilsKotlin.getTime() } returns 1

        assertThat(UtilsKotlin.getTime(), equalTo(1L))
    }

    @Test
    fun `test mocking object`() {
        val someObject = object {
            fun getTime() = System.currentTimeMillis()
        }

        mockkObject(someObject)
        every { someObject.getTime() } returns 1L

        assertThat(someObject.getTime(), equalTo(1L))
    }

    //Mockowanie konstruktora

    @Test
    fun `test mocking constructor`() {
        mockkConstructor(Time::class)
        every { anyConstructed<Time>().getCurrentTime() } returns 1

        val time = Time()
        assertThat(time.getCurrentTime(), equalTo(1L))
    }

    //Mockowanie z matcherem
    @Test
    fun `test mock with matcher`() {
        val storage = mockk<TextStorage> {
            every { getText(any()) } returns "any other"
            every { getText(isNull()) } returns "nullText"
            every { getText(less(5)) } returns "smaller than 5"
            every { getText(more(1000)) } returns "grater than 1000"
            every { getText(5) } returns "exactly 5"
            every { getText(range(10, 50)) } returns "between 10 and 50 inclusive"
            every {
                getText(range(60, 100, false, false))
            } returns "between 60 and 100 exclusive"

            every {
                getText(and(range(200, 300), match { it % 2 == 0 }))
            } returns "between 200 and 300 and it is even number"

            every { getText(or(110, 120)) } returns "it is 110 or 120"
        }

        assertThat(storage.getText(null), equalTo("nullText"))
        assertThat(storage.getText(4), equalTo("smaller than 5"))
        assertThat(storage.getText(1001), equalTo("grater than 1000"))
        assertThat(storage.getText(5), equalTo("exactly 5"))

        assertThat(storage.getText(10), equalTo("between 10 and 50 inclusive"))
        assertThat(storage.getText(20), equalTo("between 10 and 50 inclusive"))
        assertThat(storage.getText(50), equalTo("between 10 and 50 inclusive"))

        assertThat(storage.getText(60), not(equalTo("between 60 and 100 exclusive")))
        assertThat(storage.getText(80), equalTo("between 60 and 100 exclusive"))
        assertThat(storage.getText(100), not(equalTo("between 60 and 100 exclusive")))

        assertThat(storage.getText(202), equalTo("between 200 and 300 and it is even number"))
        assertThat(storage.getText(256), equalTo("between 200 and 300 and it is even number"))
        assertThat(storage.getText(207), not(equalTo("between 200 and 300 and it is even number")))

        assertThat(storage.getText(110), equalTo("it is 110 or 120"))
        assertThat(storage.getText(120), equalTo("it is 110 or 120"))
    }

    //spy

    @Test
    fun `test spy`() {
        val realData = RealData()
        val spyRealData = spyk(realData)

        spyRealData.name = "unknown"
        verify { spyRealData.name = "unknown" }
    }

    @Test
    fun `test mock on spy`() {
        val realData = RealData()
        val spyRealData = spyk(realData)
        every { spyRealData.name } returns "mocked"

        spyRealData.name = "unknown"
        verify { spyRealData.name = "unknown" }
        assertThat(spyRealData.name, equalTo("mocked"))
    }

    //weryfikowwanie interakcji

    @Test
    fun `test verify`() {
        val textStorage = mockk<TextStorage> {
            every { getText(any()) } returns "mock text"
        }

        textStorage.getText(1)
        textStorage.getText(1)
        textStorage.getText(1)

        verify(atLeast = 2) { textStorage.getText(1) }
        verify(atMost = 4) { textStorage.getText(1) }
        verify(exactly = 3) { textStorage.getText(1) }
        verify { textStorage.getText(1) } //atMost = Int.MAX_VALUE,

        assertFail { verify(atLeast = 5) { textStorage.getText(1) } }
        assertFail { verify(exactly = 2) { textStorage.getText(1) } }
        assertFail { verify(atMost = 2) { textStorage.getText(1) } }
    }

    @Test
    fun `test verify all`() {
        val manager = mockk<Manager> {
            every { getText(any()) } returns "mock tekst"
            every { findRealData(any()) } returns RealData()
            every { findRealData(any(), any()) } returns RealData()
        }

        manager.findRealData("123")
        manager.getText(1)
        manager.findRealData("456")

        verifyAll {
            manager.findRealData("456")
            manager.findRealData("123")
            manager.getText(1)
        }
    }

    @Test
    fun `test mock never call`() {
        val manager = mockk<Manager>()

        verify { manager wasNot called }
    }

    @Test(expected = java.lang.AssertionError::class)
    fun `test mock never call failed`() {
        val manager = mockk<Manager> {
            every { getText(any()) } returns "mock tekst"

        }

        manager.getText(1)

        verify { manager wasNot called }
    }

    //Weryfikowanie kolejności interakcji z mockiem

    @Test
    fun `test verify Ordering_UNORDERED`() {
        val manager = mockk<Manager> {
            every { getText(any()) } returns "mock tekst"
            every { findRealData(any()) } returns RealData()
            every { findRealData(any(), any()) } returns RealData()
        }

        manager.findRealData("123")
        manager.getText(1)
        manager.findRealData("456")

        verify {
            //ordering=Ordering.UNORDERED
            manager.findRealData("456")
            manager.findRealData("123")
        }
    }

    @Test
    fun `test verify Ordering_ALL`() {
        val manager = mockk<Manager> {
            every { getText(any()) } returns "mock tekst"
            every { findRealData(any()) } returns RealData()
            every { findRealData(any(), any()) } returns RealData()
        }

        manager.findRealData("123")
        manager.getText(1)
        manager.findRealData("456")

        verify(ordering = Ordering.ALL) {
            manager.findRealData("456")
            manager.getText(1)
            manager.findRealData("123")
        }

        assertFail {
            verify(ordering = Ordering.ALL) {
                manager.findRealData("456")
                manager.findRealData("123")
            }
        }
    }

    @Test
    fun `test verify Ordering_ORDERED`() {
        val manager = mockk<Manager> {
            every { getText(any()) } returns "mock tekst"
            every { findRealData(any()) } returns RealData()
            every { findRealData(any(), any()) } returns RealData()
        }

        manager.findRealData("123")
        manager.getText(1)
        manager.findRealData("456")

        verify(ordering = Ordering.ORDERED) {
            manager.findRealData("123")
            manager.findRealData("456")
        }
    }

    @Test
    fun `test verifyOrder`() {
        val manager = mockk<Manager> {
            every { getText(any()) } returns "mock tekst"
            every { findRealData(any()) } returns RealData()
            every { findRealData(any(), any()) } returns RealData()
        }

        manager.findRealData("123")
        manager.getText(1)
        manager.findRealData("456")

        verifyOrder {
            //ordering=Ordering.ORDERED
            manager.findRealData("123")
            manager.findRealData("456")
        }
    }


    @Test
    fun `test verifyOrder inverse`() {
        val manager = mockk<Manager> {
            every { getText(any()) } returns "mock tekst"
            every { findRealData(any()) } returns RealData()
            every { findRealData(any(), any()) } returns RealData()
        }

        manager.findRealData("123")
        manager.findRealData("456")
        manager.getText(1)

        verifyOrder(inverse = true) {
            //ordering=Ordering.ORDERED
            manager.findRealData("456")
            manager.findRealData("123")
        }
    }

    @Test
    fun `test verify Ordering_SEQUENCE`() {
        val manager = mockk<Manager> {
            every { getText(any()) } returns "mock tekst"
            every { findRealData(any()) } returns RealData()
            every { findRealData(any(), any()) } returns RealData()
        }

        manager.findRealData("123")
        manager.getText(1)
        manager.findRealData("456")

        verify(ordering = Ordering.SEQUENCE) {
            manager.findRealData("123")
            manager.getText(1)
            manager.findRealData("456")
        }

        assertFail {
            verify(ordering = Ordering.SEQUENCE) {
                manager.findRealData("123")
                manager.getText(1)
            }
        }
    }


    @Test
    fun `test verifySequence`() {
        val manager = mockk<Manager> {
            every { getText(any()) } returns "mock tekst"
            every { findRealData(any()) } returns RealData()
            every { findRealData(any(), any()) } returns RealData()
        }

        manager.findRealData("123")
        manager.getText(1)
        manager.findRealData("456")

        verifySequence {
            manager.findRealData("123")
            manager.getText(1)
            manager.findRealData("456")
        }

        assertFail {
            verifySequence {
                manager.findRealData("123")
                manager.getText(1)
            }
        }
    }

    //Przechwytywanie argumentów

    @Test
    fun `test capture to slot`() {
        val slot = slot<Int>()

        val manager = mockk<Manager> {
            every { getText(capture(slot)) } returns "mock tekst"
        }

        manager.getText(100)
        manager.getText(10)
        manager.getText(1)

        assertThat(slot.captured, equalTo(1))
    }

    @Test
    fun `test capture to list`() {
        val captured = mutableListOf<Int>()

        val manager = mockk<Manager> {
            every { getText(capture(captured)) } returns "mock tekst"
        }

        manager.getText(100)
        manager.getText(10)
        manager.getText(1)

        assertThat(captured, contains(100, 10, 1))
    }

    //Użycie matcherów

    @Test
    fun `test verify matcher`() {
        val manager = mockk<Manager>(relaxed = true) {
            every { getText(any()) } returns "mock tekst"
            every { findRealData(any()) } returns RealData()
            every { findRealData(any(), any()) } returns RealData()
        }


        val data = RealData()
        manager.getText(100)
        manager.getText(10)
        manager.getText(1)
        manager.addObject(data)

        verify { manager.getText(less(1000)) }
        verify { manager.getText(match { it == 10 }) }
        verify { manager.getText(more(0)) }
        verify { manager.getText(more(0)) }
        verify { manager.getText(or(1, 2)) }
        verify { manager.getText(range(1, 100)) }
        verify { manager.addObject(ofType(RealData::class)) }

        verify { manager.addObject(refEq(data)) }
        assertFail { verify { manager.addObject(refEq(RealData())) } }
    }


    fun assertFail(action: () -> Unit) {
        assertFail(AssertionError::class, action)
    }

    fun <T : Throwable> assertFail(exceptionClass: KClass<T>, action: () -> Unit) {
        try {
            action.invoke()
        } catch (throwable: Throwable) {
            if (throwable::class == exceptionClass) {
                return
            }
            fail("Should throw ${exceptionClass.simpleName} but throw ${throwable::class.simpleName}")
        }
        fail("Should throw ${exceptionClass.simpleName} but not throw any exception")
    }


}