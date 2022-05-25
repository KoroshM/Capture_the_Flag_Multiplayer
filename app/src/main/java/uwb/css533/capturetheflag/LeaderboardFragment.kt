package uwb.css533.capturetheflag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

// Final fragment in the loop, displays game results and navigates back to SignedInFragment
class LeaderboardFragment(private val model: MyViewModel,
                          private val winTime: Long,
                          private val bestTime: Long,
                          private val finalTime: Long) : Fragment(R.layout.leaderboard)  {

    private val TAG = "PostGame"
    private var textTimeTop: TextView? = null
    private var textTimeWin: TextView? = null
    private var textTimeUsr: TextView? = null
    private var btnBack: Button? = null

    // Main method
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG,"Entering post-game screen.")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.leaderboard, container, false)

        textTimeTop = view.findViewById(R.id.frag_textview_lb_time_top)
        textTimeWin = view.findViewById(R.id.frag_textview_lb_time_winner)
        textTimeUsr = view.findViewById(R.id.frag_textview_lb_time_player)
        btnBack = view.findViewById(R.id.frag_button_return)

        textTimeTop?.text = getFormattedStopWatch(bestTime)
        textTimeWin?.text = getFormattedStopWatch(winTime)
        textTimeUsr?.text = getFormattedStopWatch(finalTime)

        // Return to SignedInFragment (Create/Join room screen)
        btnBack?.setOnClickListener {
            model.clearSessionId()
            val navLogin = activity as FragmentNavigation
            navLogin.replaceFragment(SignedInFragment(model))
        }

        return view
    }

    // Format time in ms to a string of HH : MM : SS
    private fun getFormattedStopWatch(milliseconds: Long): String {
        var ms = milliseconds
        val hours = ms.milliseconds.inWholeHours
        ms -= hours.hours.inWholeMilliseconds
        val minutes = ms.milliseconds.inWholeMinutes
        ms -= minutes.minutes.inWholeMilliseconds
        val seconds = ms.milliseconds.inWholeSeconds
        ms -= seconds.seconds.inWholeMilliseconds

        return "${if (hours < 10) "0" else ""}$hours : " +
                "${if (minutes < 10) "0" else ""}$minutes : " +
                "${if (seconds < 10) "0" else ""}$seconds"
    }
}