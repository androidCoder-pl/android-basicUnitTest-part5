package pl.androidcoder.mockk.reference

data class RealData(
    val id: String = "realObjectId",
    var name: String? = "realName",
    val intNum: Int = -1,
    val doubleNum: Double = -1.0
) {
    fun reverseName() {
        name = name?.reversed()
    }
}

interface TextStorage{
    fun getText(someNumber : Int?) : String
}

interface Manager{
    fun findRealData(id : String) : RealData?
    fun findRealData(id : String, name: String?) : RealData?
    fun getText(someNumber : Int?) : String
    fun addObject(obj : Any)
}

interface FirstInterface{
    fun firstAction()
}

interface SecondInterface {
    fun secondAction()

}
