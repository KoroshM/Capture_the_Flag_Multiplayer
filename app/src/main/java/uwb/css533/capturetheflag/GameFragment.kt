package uwb.css533.capturetheflag

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import java.net.HttpURLConnection
import java.net.URL
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

// The fragment that runs the active game in progress
class GameFragment(private val model: MyViewModel,
                   private val flag: String,
                   private val feat1: String,
                   private val feat2: String,
                   private val feat3: String,
                   private val startTime: Long) : Fragment(R.layout.game)  {

    private val TAG = "Game"
    private var textCountry: TextView? = null
    private var btnScan: Button? = null
    private var imgFlag: ImageView? = null
    private var textTime: TextView? = null

    private var textQr1: TextView? = null
    private var textQr2: TextView? = null
    private var textQr3: TextView? = null
    private var qr1Slash: View? = null
    private var qr2Slash: View? = null
    private var qr3Slash: View? = null
    private var scan1 = false
    private var scan2 = false
    private var scan3 = false

    private val timerInterval = 1000L       // Timer update in ms
    private var mHandler: Handler? = null   // Manages the timer
    private var winTime = Long.MAX_VALUE    // Winner's time-to-finish
    private var bestTime = Long.MAX_VALUE   // Leaderboard's top time-to-finish
    private var finalTime = Long.MAX_VALUE  // Player's time-to-finish

    // Timer method as an object
    private var mTimerHandler: Runnable = object : Runnable {
        override fun run() {
            try {
                updateStopWatchView(System.currentTimeMillis() - startTime)
            } finally {
                mHandler!!.postDelayed(this, timerInterval)
            }
        }
    }

    // Main method
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.game, container, false)
        val atlas = Atlas()
        val country = atlas.get(flag)

        textCountry = view.findViewById(R.id.frag_textview_country)
        btnScan = view.findViewById(R.id.frag_button_scan)
        imgFlag = view.findViewById(R.id.frag_imageview_flag)
        textTime = view.findViewById(R.id.frag_textview_time)

        textQr1 = view.findViewById(R.id.frag_textview_qr1_code)
        textQr2 = view.findViewById(R.id.frag_textview_qr2_code)
        textQr3 = view.findViewById(R.id.frag_textview_qr3_code)
        qr1Slash = view.findViewById(R.id.frag_qr1_slash)
        qr2Slash = view.findViewById(R.id.frag_qr2_slash)
        qr3Slash = view.findViewById(R.id.frag_qr3_slash)

        // Set the flag and start the timer
        if(country == null) {
            Log.e(TAG,"Flag not supported by client.")
            return view
        } else {
            Backend.setFlag(imgFlag, country)
            textCountry?.text = country.getName()
            startClock()
        }

        // Inputs have first char as the QR# and the rest as the feature description (not used)
        textQr1?.text = feat1[0].toString()
        textQr2?.text = feat2[0].toString()
        textQr3?.text = feat3[0].toString()

        // Start a QR scan
        btnScan?.setOnClickListener {
            val intentIntegrator = IntentIntegrator.forSupportFragment(this)
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            intentIntegrator.initiateScan()
        }

        return view
    }

    // Get the response from a QR scan
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        // Parse the result
        if (result != null) {
            if (result.contents == null) {
                Log.e(TAG,"Cancelled")
                Toast.makeText(activity, "QR scan cancelled.", Toast.LENGTH_LONG).show()
            } else {
                Log.i(TAG,"Scan contents: " + result.contents)
                Toast.makeText(activity, "Scanned code: " + result.contents, Toast.LENGTH_LONG).show()

                // Update client UI on a successful scan
                updateClientGame(result.contents as String)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    // On receiving a successful QR scan check if the scanned code is correct for this flag
    private fun updateClientGame(qr: String) {
        // Build URL
        val urlStart = URL("http://" +
                model.getIP() + ":" +
                model.getPort() +
                "/capture_the_flag/check_feature?" +
                "session_id=" + model.getSessionId() +
                "&user_id=" + model.getUser()?.getId() +
                "&feature=" + qr)
        val response = StringBuffer()

        // Parse response into a StringBuffer
        with(urlStart.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET

            Log.i(TAG,"Sent 'GET' request to URL : $urlStart; Response Code : $responseCode")

            inputStream.bufferedReader().use {
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                Log.i(TAG,"Response : $response")
            }
        }

        // Response is which feature the scan corresponds to, if any
        when (response.toString()) {
            "1" -> scan1 = true
            "2" -> scan2 = true
            "3" -> scan3 = true
        }
        // Set UI slashes
        qr1Slash?.visibility = if(scan1) View.VISIBLE else View.INVISIBLE
        qr2Slash?.visibility = if(scan2) View.VISIBLE else View.INVISIBLE
        qr3Slash?.visibility = if(scan3) View.VISIBLE else View.INVISIBLE

        // Once all 3 features have been scanned call for endgame with the current time
        if(scan1 && scan2 && scan3) {
            finalTime = System.currentTimeMillis()  - startTime
            mHandler?.removeCallbacks(mTimerHandler)
            updateStopWatchView(finalTime)

            // Keep checking if game has ended
            while(!endGame()) {}

            // Game has ended
            val navLogin = activity as FragmentNavigation
            navLogin.replaceFragment(LeaderboardFragment(model,
                winTime,
                bestTime,
                finalTime))
        }
    }

    // Final method called when all 3 QR codes have been scanned and verified
    // Navigates to LeaderboardFragment
    private fun endGame(): Boolean {
        // Build URL
        val urlStart = URL("http://" +
                model.getIP() + ":" +
                model.getPort() +
                "/capture_the_flag/end_game?" +
                "session_id=" + model.getSessionId())
        val response = StringBuffer()

        // Parse response into a StringBuffer
        with(urlStart.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional, default is GET

            Log.i(TAG,"Sent 'GET' request to URL : $urlStart; Response Code : $responseCode")

            inputStream.bufferedReader().use {
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                Log.i(TAG,"Response : $response")
            }
        }

        /** Response
         * WinnerTime,BestTime
         */
        val lines = response.split(",").toTypedArray()

        // If a positive value is received then the game has ended
        return if(lines[0].toInt() > 0) {
            winTime = lines[0].toLong()
            bestTime = lines[1].toLong()
            true

        } else {
            false
        }
    }

    // Start the timer
    private fun startClock() {
        mHandler = Handler(Looper.getMainLooper())
        mTimerHandler.run()
    }

    // Update the timer ImageView with the current time elapsed
    private fun updateStopWatchView(milliseconds: Long) {
        val formattedTime = getFormattedStopWatch(milliseconds)
        textTime?.text = formattedTime
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