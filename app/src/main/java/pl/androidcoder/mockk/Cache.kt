package pl.androidcoder.mockk

import android.content.Context
import androidx.core.content.edit

class Cache(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("cache", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        sharedPreferences.edit {
            putString("name", user.name)
            putString("lastName", user.lastName)
        }
    }

    fun getUser(): User {
        val name = sharedPreferences.getString("name", "")
        val lastName = sharedPreferences.getString("lastName", "")
        return User(name, lastName)
    }
}