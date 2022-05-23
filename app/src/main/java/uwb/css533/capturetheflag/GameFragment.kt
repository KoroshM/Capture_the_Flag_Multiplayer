package uwb.css533.capturetheflag

import android.content.Intent
import android.icu.util.TimeUnit
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
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
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import java.net.HttpURLConnection
import java.net.URL
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class GameFragment(private val model: MyViewModel,
                   private val flag: String,
                   private val feat1: String,
                   private val feat2: String,
                   private val feat3: String,
                   private val startTime: String,
                   private val roomCode: String) : Fragment(R.layout.game)  {

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
    private var qr1: String? = null
    private var qr2: String? = null
    private var qr3: String? = null
    private var scan1 = false
    private var scan2 = false
    private var scan3 = false

    private val timerInterval = 1000L // Timer update in ms
    private var mHandler: Handler? = null
    private var finalTime = Long.MAX_VALUE

    private var mTimerHandler: Runnable = object : Runnable {
        override fun run() {
            try {
                updateStopWatchView(System.currentTimeMillis() - startTime.toLong())
            } finally {
                mHandler!!.postDelayed(this, timerInterval)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.game, container, false)
        val atlas = Atlas()
        val country = atlas.get(flag)

        textCountry = view.findViewById<TextView>(R.id.frag_textview_country)
        btnScan = view.findViewById<Button>(R.id.frag_button_scan)
        imgFlag = view.findViewById<ImageView>(R.id.frag_imageview_flag)
        textTime = view.findViewById<TextView>(R.id.frag_textview_time)

        textQr1 = view.findViewById<TextView>(R.id.frag_textview_qr1_code)
        textQr2 = view.findViewById<TextView>(R.id.frag_textview_qr2_code)
        textQr3 = view.findViewById<TextView>(R.id.frag_textview_qr3_code)
        qr1Slash = view.findViewById<View>(R.id.frag_qr1_slash)
        qr2Slash = view.findViewById<View>(R.id.frag_qr2_slash)
        qr3Slash = view.findViewById<View>(R.id.frag_qr3_slash)

        if(country == null) {
            Log.e(TAG,"Error loading flag from server.")
            return view
        } else {
            Backend.setFlag(imgFlag, country)
            textCountry?.text = country.getName()
            startClock()
        }

        qr1 = feat1[0].toString()
        qr2 = feat2[0].toString()
        qr3 = feat3[0].toString()
        textQr1?.text = qr1
        textQr2?.text = qr2
        textQr3?.text = qr3

        startClock()
        btnScan?.setOnClickListener {
            val intentIntegrator = IntentIntegrator.forSupportFragment(this)
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            intentIntegrator.initiateScan()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents == null) {
                Log.e(TAG,"Cancelled")
                Toast.makeText(activity, "QR scan cancelled.", Toast.LENGTH_LONG).show()
            } else {
                Log.i(TAG,"Scan contents: " + result.contents)
                Toast.makeText(activity, "scanned: " + result.contents, Toast.LENGTH_LONG).show()

                updateClientGame(result.contents as String)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun updateClientGame(qr: String) {
        val urlStart = URL("http://" +
                model.getIP() + ":" +
                model.getPort() +
                "/capture_the_flag/check_feature?" +
                "session_id=" + roomCode +
                "&user_id=" + model.getUser()?.getId() +
                "&feature=" + qr)
        val response = StringBuffer()

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

        when (response.toString()) {
            qr1 -> scan1 = true
            qr2 -> scan2 = true
            qr3 -> scan3 = true
        }
        qr1Slash?.visibility = if(scan1) View.VISIBLE else View.INVISIBLE
        qr2Slash?.visibility = if(scan2) View.VISIBLE else View.INVISIBLE
        qr3Slash?.visibility = if(scan3) View.VISIBLE else View.INVISIBLE

        if(scan1 && scan2 && scan3) {
            finalTime = System.currentTimeMillis()
            endGame()
        }
    }

    private fun endGame() {
        val urlStart = URL("http://" +
                model.getIP() + ":" +
                model.getPort() +
                "/capture_the_flag/end_game?" +
                "session_id=" + roomCode)
        val response = StringBuffer()

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
        if(response.toString().toInt() > 0) {
            // Game has ended
            mHandler?.removeCallbacks(mTimerHandler)
            val navLogin = activity as FragmentNavigation
            navLogin.replaceFragment(LeaderboardFragment(model, finalTime, roomCode))
        }
    }

    private fun startClock() {
        mHandler = Handler(Looper.getMainLooper())
        mTimerHandler.run()
    }

    private fun updateStopWatchView(milliseconds: Long) {
        val formattedTime = getFormattedStopWatch(milliseconds)
        textTime?.text = formattedTime
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