package uwb.css533.capturetheflag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.net.HttpURLConnection
import java.net.URL

// Fragment that requests the user credentials be assigned to a given room
// Navigates to GameFragment on success
class JoinRoomFragment(private val model: MyViewModel) : Fragment(R.layout.join_screen)  {

    private val TAG = "Join"
    private var roomId: EditText? = null
    private var btnSearch: Button? = null
    private var textFound: TextView? = null

    // Main method
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG,"Entering Join Fragment")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_signedin, container, false)

        roomId = view.findViewById(R.id.frag_edittext_sessionID)
        btnSearch = view.findViewById(R.id.frag_button_search)
        textFound = view.findViewById(R.id.frag_textview_found)

        // Attempt to join the input room number
        btnSearch?.setOnClickListener {
            val roomCode = roomId?.text
            if(roomCode != null) {
                // Join room
                val url1 = URL("http://" +
                        model.getIP() + ":" +
                        model.getPort() +
                        "/capture_the_flag/join?" +
                        "user_id=" + model.getUser()?.getId() +
                        "&session_id=" + roomCode)
                val response1 = StringBuffer()

                // Parse response into a StringBuffer
                with(url1.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"  // optional, default is GET

                    Log.i(TAG,"Sent 'GET' request to URL : $url; Response Code : $responseCode")

                    inputStream.bufferedReader().use {
                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response1.append(inputLine)
                            inputLine = it.readLine()
                        }
                        Log.i(TAG,"Response : $response1")
                    }
                }

                if(!response1.toString().startsWith("-")) {
                    // Build URL
                    val url2 = URL(
                        "http://" +
                                model.getIP() + ":" +
                                model.getPort() +
                                "/capture_the_flag/start_game?" +
                                "user_id=" + model.getUser()?.getId() +
                                "&session_id=" + model.getSessionId()
                    )
                    val response2 = StringBuffer()

                    // Parse response into a StringBuffer
                    with(url2.openConnection() as HttpURLConnection) {
                        requestMethod = "GET"  // optional, default is GET

                        Log.i(TAG, "Sent 'GET' request to URL : $url; Response Code : $responseCode")

                        inputStream.bufferedReader().use {
                            var inputLine = it.readLine()
                            while (inputLine != null) {
                                response2.append(inputLine)
                                inputLine = it.readLine()
                            }
                            Log.i(TAG, "Response : $response2")
                        }
                    }

                    /** Response
                     * Country,(QR1)Feature1,(QR2)Feature2,(QR3)Feature3,(Start time)
                     */
                    // Store response into an array of parameters
                    val lines = response2.split(",").toTypedArray()

                    // Begin the game
                    val navLogin = activity as FragmentNavigation
                    navLogin.replaceFragment(GameFragment(model,
                            lines[0],   // Country
                            lines[1],   // [QR1]Feature1
                            lines[2],   // [QR2]Feature2
                            lines[3],   // [QR3]Feature3
                            lines[4].toLong())) // Start time in ms

                } else {
                    Log.e(TAG,"Error joining room: room does not exist")
                }
            } else {
                Log.e(TAG, "Error joining room: null room code input")
            }
            Toast.makeText(activity,"Invalid room code, please try again.",Toast.LENGTH_SHORT).show()
        }

        return view
    }
}