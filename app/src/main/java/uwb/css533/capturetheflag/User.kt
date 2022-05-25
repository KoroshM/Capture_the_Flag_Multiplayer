package uwb.css533.capturetheflag

// Class used to store player account info, mostly relevant for the secret user ID
class User(user: String, pass: String, id: String) {
    private val username: String = user
    private val password: String = pass
    private val userId: String = id

    fun getUsername(): String {
        return username
    }

    fun getPass(): String {
        return password
    }

    fun getId(): String {
        return userId
    }
}