package uwb.css533.capturetheflag

import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

    private var user: User? = null

    private var _auth = MutableLiveData(false)
    var auth: LiveData<Boolean> = _auth

    fun setUser(newUser: User) {
        user = newUser
    }

    fun setSignedIn(newValue : Boolean) {
        // use postvalue() to make the assignation on the main (UI) thread
        _auth.postValue(newValue)
    }

    fun getUser(): User? {
        return user
    }
}