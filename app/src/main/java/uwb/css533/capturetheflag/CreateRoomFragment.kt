package uwb.css533.capturetheflag

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment


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
        textSearching?.visibility = View.VISIBLE
        btnStart?.visibility = View.GONE

        var fullLobby = 0
        while(fullLobby == 0) {
            // TODO: Heartbeat to check if game started
            val response = "1" // ------------------------------- GS returns full or not 1/0
            fullLobby = response.toInt()
        }
        textSearching?.visibility = View.INVISIBLE
        btnStart?.visibility = View.VISIBLE

        btnStart?.setOnClickListener {
            var start = "0" // Starts at 0, should be set to whatever the start time was of the room timer
            while(start == "0") {
                // TODO: Heartbeat to check if game started
                val response = System.currentTimeMillis().toString() // ------------------------------- GS returns 0 or start time
                start = response
            }
            textSearching?.text = R.string.starting_game.toString()
            textSearching?.visibility = View.VISIBLE

            val navLogin = activity as FragmentNavigation
            navLogin.replaceFragment(GameFragment(model, start))
        }

        return returnView
    }
}