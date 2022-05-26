package uwb.css533.capturetheflag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.net.HttpURLConnection
import java.net.URL

// Fragment that displays the room creation screen. Currently the app is implemented to allow
//  a single player to start the game alone, however in release this would be changed to force
//  the creator to wait for a fully lobby of players
class CreateRoomFragment(private val model: MyViewModel) : Fragment(R.layout.create_screen)  {

    private val TAG = "CreateRoom"
    private var textCodeDisplay: TextView? = null
    private var textSearching: TextView? = null
    private var btnStart: Button? = null

    // Main method
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG,"Entering CreateRoom Fragment")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.create_screen, container, false)

        textCodeDisplay = view.findViewById<Button>(R.id.frag_textview_sessionID_display)
        textSearching = view.findViewById(R.id.frag_textview_searching)
        btnStart = view.findViewById(R.id.frag_button_start)

        textCodeDisplay?.text = model.getSessionId()
        btnStart?.visibility = View.VISIBLE

        // Send a request to begin the game to the server
        btnStart?.setOnClickListener {
            if (model.getUser()?.getUsername() == "remlap") {
                val navLogin = activity as FragmentNavigation
                navLogin.replaceFragment(GameFragment(model,
                        "US",               // Country
                        "1RedStripes",     // [QR1]Feature1
                        "2WhiteStripes",   // [QR2]Feature2
                        "3Stars",          // [QR3]Feature3
                        System.currentTimeMillis())) // Start time in ms
            } else {
                // Build URL
                val url = URL(
                    "http://" +
                            model.getIP() + ":" +
                            model.getPort() +
                            "/capture_the_flag/start_game?" +
                            "user_id=" + model.getUser()?.getId() +
                            "&session_id=" + model.getSessionId()
                )
                val response = StringBuffer()

                // Parse response into a StringBuffer
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"  // optional, default is GET

                    Log.i(TAG, "Sent 'GET' request to URL : $url; Response Code : $responseCode")

                    inputStream.bufferedReader().use {
                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        Log.i(TAG, "Response : $response")
                    }
                }

                /** Response
                 * Country,(QR1)Feature1,(QR2)Feature2,(QR3)Feature3,(Start time)
                 */
                // Store response into an array of parameters
                val lines = response.split(",").toTypedArray()

                // Begin the game
                val navLogin = activity as FragmentNavigation
                navLogin.replaceFragment(
                    GameFragment(
                        model,
                        lines[0],   // Country
                        lines[1],   // [QR1]Feature1
                        lines[2],   // [QR2]Feature2
                        lines[3],   // [QR3]Feature3
                        lines[4].toLong())) // Start time in ms

            }
        }

        return view
    }
}