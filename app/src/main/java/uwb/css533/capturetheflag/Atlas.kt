package uwb.css533.capturetheflag

// Class to store a list of supported Countries for the game and a couple getter methods
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
        Country("NZ", "New Zealand"),
        Country("FR", "France"),
        Country("NL", "Netherlands"),
        Country("RU","Russia"))

    // Randomly return a country Pair
    fun getRandom(): Country {
        return atlas.random()
    }

    // Get a Country object based on its Code or Name
    fun get(country: String): Country? {
        for(item in atlas) {
            if(item.getCode() == country || item.getName() == country)
                return item
        }
        return null
    }
}