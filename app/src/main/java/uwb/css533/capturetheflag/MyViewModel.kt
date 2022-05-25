package uwb.css533.capturetheflag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Class used to store data from fragment to fragment
class MyViewModel(private val ip: String,
                  private val port: Int) : ViewModel() {

    // Currently active session ID, null unless actively in a game
    private var room: String? = null
    // User object storing player's credentials
    private var user: User? = null
    // Whether the user is signed in
    private var _auth = MutableLiveData(false)
    var auth: LiveData<Boolean> = _auth

    fun setSignedIn(newValue : Boolean) {
        _auth.postValue(newValue)
    }

    fun setSessionId(newValue: String) {
        room = newValue
    }

    fun clearSessionId() {
        room = null
    }

    fun getSessionId(): String? {
        return room
    }

    fun setUser(newUser: User) {
        user = newUser
    }

    fun getUser(): User? {
        return user
    }

    fun clearUser() {
        user = null
    }

    fun getIP(): String {
        return ip
    }

    fun getPort(): String {
        return port.toString()
    }
}