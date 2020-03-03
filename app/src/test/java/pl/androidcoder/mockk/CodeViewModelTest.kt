package pl.androidcoder.mockk

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class CodeViewModelTest {
    @Before
    fun setUp() {
        mockkConstructor(CodeMapper::class)
        every { anyConstructed<CodeMapper>().map(1) } returns "one"
        every { anyConstructed<CodeMapper>().map(2) } returns "two"
    }

    @Test
    fun name() {
        val codeViewModel = CodeViewModel(mockk())
        codeViewModel.setCode(1)
        assertThat(codeViewModel.errorText, equalTo("one"))
        codeViewModel.setCode(2)
        assertThat(codeViewModel.errorText, equalTo("two"))
    }
}