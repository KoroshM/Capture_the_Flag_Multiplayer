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


class CreateRoomFragment(private val model: MyViewModel, private val roomCode: String) : Fragment(R.layout.create_screen)  {

    private val TAG = "CreateRoom"
    private var textCodeDisplay: TextView? = null
    private var textSearching: TextView? = null
    private var btnStart: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG,"Entering CreateRoom Fragment")
        // Inflate the layout for this fragment
        val returnView = inflater.inflate(R.layout.create_screen, container, false)
        textCodeDisplay = returnView.findViewById<Button>(R.id.frag_textview_sessionID_display)
        textSearching = returnView.findViewById<TextView>(R.id.frag_textview_searching)
        btnStart = returnView.findViewById<Button>(R.id.frag_button_start)

        textCodeDisplay?.text = roomCode
        btnStart?.visibility = View.VISIBLE

        btnStart?.setOnClickListener {
            lateinit var lines: List<String>

            val urlStart = URL("http://" +
                    model.getIP() + ":" +
                    model.getPort() +
                    "/capture_the_flag/start_game?" +
                    "user_id=" + model.getUser()?.getId() +
                    "&session_id=" + roomCode)
            val responseStart = StringBuffer()

            with(urlStart.openConnection() as HttpURLConnection) {
                requestMethod = "GET"  // optional default is GET

                Log.i(TAG,"Sent 'GET' request to URL : $urlStart; Response Code : $responseCode")

                inputStream.bufferedReader().use {
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        responseStart.append(inputLine)
                        inputLine = it.readLine()
                    }
                    Log.i(TAG,"Response : $responseStart")
                }
            }

            /** Response
             * Country
             * (QR1)Feature1
             * (QR2)Feature2
             * (QR3)Feature3
             * (Start time)
             */
            lines = responseStart.lines()

            val navLogin = activity as FragmentNavigation
            navLogin.replaceFragment(GameFragment(model,
                lines[0],
                lines[1],
                lines[2],
                lines[3],
                lines[4],
                roomCode))
        }

        return returnView
    }
}