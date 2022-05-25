package uwb.css533.capturetheflag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import java.net.HttpURLConnection
import java.net.URL

// Fragment that allows a user to create a game session or navigates to a join screen
class SignedInFragment(private val model: MyViewModel) : Fragment(R.layout.activity_signedin)  {

    private val TAG = "SignedIn"
    private var btnCreate: Button? = null
    private var btnJoin: Button? = null

    // Main method
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG,"Entering SignedIn Fragment")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_signedin, container, false)

        btnCreate = view.findViewById(R.id.frag_button_create)
        btnJoin = view.findViewById(R.id.frag_button_join)

        // Attempts to create a new room with the current user credentials
        btnCreate?.setOnClickListener {
            val roomCode = createRoom(model.getUser())

            // Proceed to CreateFragment on success
            if(roomCode.startsWith("-")) {
                Log.e(TAG, "Unable to create room.")
                model.clearSessionId()

            } else {
                model.setSessionId(roomCode)
                val navLogin = activity as FragmentNavigation
                navLogin.replaceFragment(CreateRoomFragment(model))
            }
        }

        // Navigates user to JoinFragment
        btnJoin?.setOnClickListener {

            val navLogin = activity as FragmentNavigation
            navLogin.replaceFragment(JoinRoomFragment(model))
        }

        return view
    }

    // If user navigates back to this page
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        model.clearSessionId()
    }

    // Method that requests the server for a room ID on successful creation
    private fun createRoom(user: User?): String {
        // Fail if there is no stored user data
        if(user == null) {
            return "-999"
        }

        if (model.getUser()?.getUsername() == "remlap") {
            return "TEST"
        }

        // Build URL
        val url = URL("http://" +
                model.getIP() + ":" +
                model.getPort() +
                "/capture_the_flag/new_room?" +
                "user_id=" + model.getUser()?.getId())
        val response = StringBuffer()

        // Parse response into a StringBuffer
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
        return response.toString()
    }
}