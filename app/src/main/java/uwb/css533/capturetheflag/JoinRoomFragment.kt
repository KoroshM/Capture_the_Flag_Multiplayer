package uwb.css533.capturetheflag

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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


class JoinRoomFragment(private val model: MyViewModel) : Fragment(R.layout.join_screen)  {

    private val TAG = "Join"
    private var roomId: EditText? = null
    private var btnSearch: Button? = null
    private var textFound: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG,"Entering Join Fragment")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_signedin, container, false)
        roomId = view.findViewById<EditText>(R.id.frag_edittext_sessionID)
        btnSearch = view.findViewById<Button>(R.id.frag_button_search)
        textFound = view.findViewById<TextView>(R.id.frag_textview_found)

        btnSearch?.setOnClickListener {
            val roomCode = roomId?.text
            lateinit var lines: List<String>
            if(roomCode != null) {
                // Join room
                val url = URL("http://" +
                        model.getIP() + ":" +
                        model.getPort() +
                        "/capture_the_flag/join?" +
                        "user_id=" + model.getUser()?.getId() +
                        "&session_id=" + roomCode)
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

                /** Response **
                 * Country
                 * (QR)Feature1
                 * (QR)Feature2
                 * (QR)Feature3
                 * (Start time)
                 */
                lines = response.lines()

                val navLogin = activity as FragmentNavigation
                navLogin.replaceFragment(GameFragment(model,
                    lines[0],
                    lines[1],
                    lines[2],
                    lines[3],
                    lines[4],
                    roomCode.toString()))
            } else {
                Log.e(TAG, "Error joining room: null code")
            }

            Toast.makeText(activity,"Invalid room code, please try again.",Toast.LENGTH_SHORT).show()
        }

        return view
    }
}