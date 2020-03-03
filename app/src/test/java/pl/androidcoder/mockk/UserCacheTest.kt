package pl.androidcoder.mockk

import android.content.Context
import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class UserCacheTest {

    private val mockContext = mockk<Context>()
    private val mockSharedPrefs = mockk<SharedPreferences>(relaxed = true) {
        every { getString("name", any()) } returns "Jon"
        every { getString("lastName", any()) } returns "Snow"
    }
    private lateinit var userCache: Cache

    @Before
    fun setUp() {
        every { mockContext.getSharedPreferences(any(), any()) } returns mockSharedPrefs
        userCache = Cache(mockContext)
    }

    @Test
    fun name() {
        val user = User("Jon", "Snow")
        userCache.saveUser(user)
        verify { mockSharedPrefs.edit().putString("name", user.name) }
        verify { mockSharedPrefs.edit().putString("lastName", user.lastName) }
    }

    @Test
    fun name2() {
        //GIVEN
        val expectedUser = User("Jon", "Snow")
        //WHEN
        val user = userCache.getUser()
        //THEN
        assertThat(user, equalTo(expectedUser))

    }
}