package pl.androidcoder.mockk

import android.content.Context

class CodeMapper(val context: Context) {
    fun map(code: Int): String {
        return when (code) {
            1 -> context.getString(R.string.item_not_found)
            2 -> context.getString(R.string.access_denied)
            3 -> context.getString(R.string.too_many_request)
            4 -> context.getString(R.string.account_deleted)
            else -> context.getString(R.string.unknown_error)
        }
    }

}

class CodeViewModel(context: Context) {

    private val errorMapper = CodeMapper(context)

    var errorText = ""

    fun setCode(code: Int) {
        errorText = errorMapper.map(code)
    }
}