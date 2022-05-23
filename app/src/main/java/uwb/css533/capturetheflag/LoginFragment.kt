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
        val view = inflater.inflate(R.layout.login_screen, container, false)
        userField = view.findViewById<EditText>(R.id.frag_edittext_username)
        passField = view.findViewById<EditText>(R.id.frag_edittext_password)
        btnLogin = view.findViewById<Button>(R.id.frag_button_login)

        btnLogin?.setOnClickListener {
            login()

            if(model.auth.equals(false)){
                Log.e(TAG,"Login failed.")
            } else {
                val navLogin = activity as FragmentNavigation
                navLogin.replaceFragment(SignedInFragment(model))
            }
        }

        return view
    }

    private fun login() {
        val userText = userField?.text.toString()
        val passText = passField?.text.toString()
        val policy = ThreadPolicy
            .Builder()
            .permitAll()
            .build()
        StrictMode.setThreadPolicy(policy)

        if(userText == "null" || passText == "null") {
            Toast.makeText(activity,"Please enter a username and password.",Toast.LENGTH_SHORT).show()
            return
        }

        val url = URL("http://" +
                model.getIP() + ":" +
                model.getPort() +
                "/capture_the_flag/login?" +
                "username=" + userText +
                "&password=" + passText)
        val response = StringBuffer()

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET

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
        val success = response.toString().toInt() > 0

        if(success) {
            model.setUser(User(userText, passText, response.toString()))
        }
        model.setSignedIn(success)
    }
}