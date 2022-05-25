package uwb.css533.capturetheflag

// Class to store country information in one bundled object
class Country(private val code: String,
              private val name: String) {
    fun getName(): String {
        return name
    }

    fun getCode(): String {
        return code
    }
}