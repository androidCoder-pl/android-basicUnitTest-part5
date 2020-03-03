package pl.androidcoder.mockk

interface UserProvider {
    fun getUser(): User
}

class UserViewModelUpdater(
    private val userProvider: UserProvider,
    private val viewModel: UserViewModel
) {

    fun updateUser() {
        val user = userProvider.getUser()
        viewModel.setUser(user)
    }

}