package uwb.css533.capturetheflag

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.net.HttpURLConnection
import java.net.URL

// Fragment that takes in user input and makes an auth request to the server
class LoginFragment(private val model: MyViewModel) : Fragment()  {

    private val TAG = "Login"
    private var userField: EditText? = null
    private var passField: EditText? = null
    private var btnLogin: Button? = null

    fun LoginFragment() {
        // Required empty public constructor
    }

    // Main method
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG,"Entering Login Fragment")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.login_screen, container, false)

        model.clearSessionId()
        model.clearUser()
        model.setSignedIn(false)
        userField = view.findViewById(R.id.frag_edittext_username)
        passField = view.findViewById(R.id.frag_edittext_password)
        btnLogin = view.findViewById(R.id.frag_button_login)

        btnLogin?.setOnClickListener {
            login()

            // Proceed if login succeeded
            if(model.auth.value == false){
                Log.e(TAG,"Login failed.")
            } else {
                val navLogin = activity as FragmentNavigation
                navLogin.replaceFragment(SignedInFragment(model))
            }
        }

        return view
    }

    // If user navigates back to this page
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        model.clearSessionId()
        model.clearUser()
    }

    // Method to check input credentials
    private fun login() {
        val userText = userField?.text.toString()
        val passText = passField?.text.toString()
        // Set internet policy
        val policy = ThreadPolicy
            .Builder()
            .permitAll()
            .build()
        StrictMode.setThreadPolicy(policy)

        // Ensure user entered something in each field
        if(userText == "" || passText == "") {
            Toast.makeText(activity,"Please enter a username and password.",Toast.LENGTH_SHORT).show()
            return
        }

        // For testing only
        if(userText == "remlap" && passText == "palmer") {
            model.setUser(User(userText, passText, "1"))
            model.setSignedIn(true)
            return
        }

        // Build URL for login request
        val url = URL("http://" +
                model.getIP() + ":" +
                model.getPort() +
                "/capture_the_flag/login?" +
                "username=" + userText +
                "&password=" + passText)
        val response = StringBuffer()

        // Parse return into a StringBuffer
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional, default is GET

            Log.i(TAG,"Sent 'GET' request to URL : $url; Response Code : $responseCode")

            inputStream.bufferedReader().use {
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                Log.i(TAG,"Response : $response")
            }
        }
        // Returns user ID for correct credentials, -1 for incorrect
        val success = response.toString().toInt() >= 0

        if(success) {
            // Save user credentials if successfully authenticated
            model.setUser(User(userText, passText, response.toString()))
        }
        model.setSignedIn(success)
    }
}