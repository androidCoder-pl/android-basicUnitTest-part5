package pl.androidcoder.mockk

class OnDataHandler {
    fun onNewData(data: String) {
        SomeSingleton.getSingleton().setData(data)
    }
}