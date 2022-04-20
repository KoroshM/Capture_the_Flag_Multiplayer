package uwb.css533.capturetheflag

class Countries {
    // Supported Countries (Code, Name)
    private val index: List<Pair<String,String>> = listOf(
        Pair("US", "United States"),
        Pair("CA", "Canada"),
        Pair("MX", "Mexico"),
        Pair("GT", "Guatemala"),
        Pair("NI", "Nicaragua"),
        Pair("CU", "Cuba"),
        Pair("PR", "Puerto Rico"),
        Pair("CO", "Colombia"),
        Pair("VE", "Venezuela"),
        Pair("EC", "Ecuador"),
        Pair("PE", "Peru"),
        Pair("BR", "Brazil"),
        Pair("GY", "Guyana"),
        Pair("SR", "Suriname"),
        Pair("PY", "Paraguay"),
        Pair("UY", "Uruguay"),
        Pair("AR", "Argentina"),
        Pair("CL", "Chile"),
        Pair("GL", "Greenland"),
        Pair("IS", "Iceland"),
        Pair("AQ", "Antarctica"),
        Pair("AU", "Australia"),
        Pair("NZ", "New Zealand"))

    // Randomly return a country Pair
    fun getCountry(): Pair<String,String> {
        return index.random()
    }

    // Return number of supported countries
    fun getSize(): Int {
        return index.size
    }
}