package pl.androidcoder.mockk

class UserViewModel {

    var userName = ""

    fun setUser(user: User) {
        userName = "${user.name} ${user.lastName}"
    }
}