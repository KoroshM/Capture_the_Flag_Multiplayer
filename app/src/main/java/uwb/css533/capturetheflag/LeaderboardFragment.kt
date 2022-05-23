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
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


class LeaderboardFragment(private val model: MyViewModel,
                          private val winTime: Long,
                          private val bestTime: Long,
                          private val finalTime: Long) : Fragment(R.layout.leaderboard)  {

    private val TAG = "PostGame"
    private var textTimeTop: TextView? = null
    private var textTimeWin: TextView? = null
    private var textTimeUsr: TextView? = null
    private var btnBack: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.game, container, false)

        textTimeTop = view.findViewById(R.id.frag_textview_lb_time_top)
        val textTimeTopDesc = view.findViewById<TextView>(R.id.textview_score1)
        textTimeWin = view.findViewById(R.id.frag_textview_lb_time_winner)
        textTimeUsr = view.findViewById(R.id.frag_textview_lb_time_player)
        btnBack = view.findViewById(R.id.frag_button_return)

        textTimeTopDesc.visibility = View.INVISIBLE

        textTimeTop?.text = getFormattedStopWatch(bestTime)
        textTimeWin?.text = getFormattedStopWatch(winTime)
        textTimeUsr?.text = getFormattedStopWatch(finalTime)

        btnBack?.setOnClickListener {
            model.clearSessionId()
            val navLogin = activity as FragmentNavigation
            navLogin.replaceFragment(SignedInFragment(model))
        }

        return view
    }

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