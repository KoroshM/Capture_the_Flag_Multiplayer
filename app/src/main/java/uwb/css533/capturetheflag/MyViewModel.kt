package uwb.css533.capturetheflag

import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel(private val ip: String, private val port: Int) : ViewModel() {

    private var room: String? = null
    private var user: User? = null
    private var _auth = MutableLiveData(false)
    var auth: LiveData<Boolean> = _auth

    fun setUser(newUser: User) {
        user = newUser
    }

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

    fun getUser(): User? {
        return user
    }

    fun getIP(): String {
        return ip
    }

    fun getPort(): String {
        return port.toString()
    }
}