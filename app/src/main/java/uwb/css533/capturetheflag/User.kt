package uwb.css533.capturetheflag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class User(user: String, pass: String, id: String) {
    private val username: String = user
    private val password: String = pass
    private val userId: String = id

    fun getUser(): String {
        return username
    }

    fun getPass(): String {
        return password
    }

    fun getId(): String {
        return userId
    }
}