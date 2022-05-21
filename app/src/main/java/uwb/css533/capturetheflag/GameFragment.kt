package uwb.css533.capturetheflag

import android.content.Intent
import android.icu.util.TimeUnit
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
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class GameFragment(private val model: MyViewModel, private val startTime: String) : Fragment(R.layout.game)  {

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
    private var qr1 = "0"
    private var qr2 = "0"
    private var qr3 = "0"

    private val timerInterval = 1000L // Timer update in ms
    private var mHandler: Handler? = null

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

        // TODO: Request country
        val flag = "US"
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

        while(qr1 <= "0" || qr2 <= "0" || qr3 <= "0") { // --------------------- Return -1 on error
            if(qr1 <= "0") {
                // TODO: Request QR code 1
                val response = "1"
                qr1 = response
            }
            if(qr2 <= "0") {
                // TODO: Request QR code 2
                val response = "2"
                qr2 = response
            }
            if(qr3 <= "0") {
                // TODO: Request QR code 3
                val response = "3"
                qr3 = response
            }
        }

        textQr1?.text = qr1.toString()
        textQr2?.text = qr2.toString()
        textQr3?.text = qr3.toString()

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

                // TODO: Check if scanned code is correct
                val response = "QR code received" // ---------------------------- Placeholder response
                updateClientGame()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun updateClientGame() {
        // TODO: Implement game state heartbeat
        // 101 = codes 1 and 3 acquired, etc
        val result = "110"

        scan1 = result[0] == '1'
        scan2 = result[1] == '1'
        scan3 = result[2] == '1'

        if(scan1 && scan2 && scan3) {
            endGame()
        } else {
            qr1Slash?.visibility = if(scan1) View.VISIBLE else View.INVISIBLE
            qr2Slash?.visibility = if(scan2) View.VISIBLE else View.INVISIBLE
            qr3Slash?.visibility = if(scan3) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun endGame() {
        mHandler?.removeCallbacks(mTimerHandler)
        val navLogin = activity as FragmentNavigation
        navLogin.replaceFragment(LeaderboardFragment(model))
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