package uwb.css533.capturetheflag

class Atlas {
    // Supported Countries (Code, Name)
    private val atlas: List<Country> = listOf(
        Country("US", "United States"),
        Country("CA", "Canada"),
        Country("MX", "Mexico"),
        Country("GT", "Guatemala"),
        Country("NI", "Nicaragua"),
        Country("CU", "Cuba"),
        Country("PR", "Puerto Rico"),
        Country("CO", "Colombia"),
        Country("VE", "Venezuela"),
        Country("EC", "Ecuador"),
        Country("PE", "Peru"),
        Country("BR", "Brazil"),
        Country("GY", "Guyana"),
        Country("SR", "Suriname"),
        Country("PY", "Paraguay"),
        Country("UY", "Uruguay"),
        Country("AR", "Argentina"),
        Country("CL", "Chile"),
        Country("GL", "Greenland"),
        Country("IS", "Iceland"),
        Country("AQ", "Antarctica"),
        Country("AU", "Australia"),
        Country("NZ", "New Zealand"))

    // Randomly return a country Pair
    fun getRandom(): Country {
        return atlas.random()
    }

    // Return number of supported countries
    fun getSize(): Int {
        return atlas.size
    }

    fun get(country: String): Country? {
        var i = 0
        while(i < atlas.size) {
            if(atlas[i].getName() == country || atlas[i].getCode() == country){
                return atlas[i]
            }
            i++
        }
        return null
    }
}