package uwb.css533.capturetheflag

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import java.util.logging.Logger


class LeaderboardFragment(private val model: MyViewModel,
                          private val time: Long,
                          private val roomCode: String) : Fragment(R.layout.leaderboard)  {

    private val TAG = "PostGame"
    private var textCountry: TextView? = null
    private var btnScan: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.game, container, false)

        btnScan = view.findViewById<Button>(R.id.frag_button_scan)

        return view
    }
}