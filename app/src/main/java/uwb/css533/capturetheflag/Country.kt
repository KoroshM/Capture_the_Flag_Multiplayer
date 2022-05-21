package uwb.css533.capturetheflag

class Country(private val code: String,
              private val name: String) {
    fun getName(): String {
        return name
    }

    fun getCode(): String {
        return code
    }
}