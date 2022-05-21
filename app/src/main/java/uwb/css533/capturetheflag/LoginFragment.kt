package uwb.css533.capturetheflag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment


class LoginFragment(private val model: MyViewModel) : Fragment()  {

    private val TAG = "Login"
    private var userField: EditText? = null
    private var passField: EditText? = null
    private var btnLogin: Button? = null

    fun LoginFragment() {
        // Required empty public constructor
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG,"Entering Login Fragment")
        // Inflate the layout for this fragment
        val returnView = inflater.inflate(R.layout.login_screen, container, false)
        userField = returnView.findViewById<EditText>(R.id.frag_edittext_username)
        passField = returnView.findViewById<EditText>(R.id.frag_edittext_password)
        btnLogin = returnView.findViewById<Button>(R.id.frag_button_login)

        btnLogin?.setOnClickListener {
            login()

            if(model.auth.equals(false)){
                Log.e(TAG,"Login failed.")
            } else {
                val navLogin = activity as FragmentNavigation
                navLogin.replaceFragment(SignedInFragment(model))
            }
        }

        return returnView
    }

    private fun login() {
        val userText = userField?.text.toString()
        val passText = passField?.text.toString()

        if(userText == "null" || passText == "null") {
            Toast.makeText(activity,"Please enter a username and password.",Toast.LENGTH_SHORT).show()
            return
        }

        // TODO: Implement login functionality
        val response = "123456"
        val success = response.toInt() > 0

        if(success) {
            model.setUser(User(userText, passText, response))
        }
        model.setSignedIn(success)
    }
}