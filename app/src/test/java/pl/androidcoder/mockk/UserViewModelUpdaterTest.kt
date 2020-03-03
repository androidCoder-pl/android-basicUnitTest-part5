package pl.androidcoder.mockk

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Test

class UserViewModelUpdaterTest {

    private val viewModel = spyk(UserViewModel())
    private val userProvider = mockk<UserProvider>()

    private val updater = UserViewModelUpdater(userProvider, viewModel)

    @Test
    fun name() {
        //GIVEN
        val user = User("John", "Doe")
        every { userProvider.getUser() } returns user
        //WHEN
        updater.updateUser()
        //THEN
        verify { viewModel.setUser(user) }
    }
}