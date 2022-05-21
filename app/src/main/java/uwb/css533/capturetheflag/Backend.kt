package uwb.css533.capturetheflag

import android.util.Log
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import com.squareup.picasso.Picasso

object Backend {
    private const val TAG = "Backend"
    private val atlas = Atlas()             // Class with pre-made list of countries
    private var autoscroll: Animation? = null               // Animator for flag carousel

    // Initialize
    fun initialize() : Backend {
        // Do any setup needed before starting any auth tasks
        return this
    }

    // Set user sign-in status
    private fun updateUserData(withSignedInStatus : Boolean) {
        UserData.setSignedIn(withSignedInStatus)
    }

    // Sign user out
    fun signOut() {
        Log.i(TAG, "Initiate Signout Sequence")

        val success = true // API_CALL_SIGNOUT HERE
        if(success) {
            Log.i(TAG, "Signed out!")
            UserData.setSignedIn(false)
        }
        else {
            Log.i(TAG, "Error signing out.")
        }
    }

    // Sign user in
    fun signIn() {
        Log.i(TAG, "Initiate Signin Sequence")

        val success = true // API_CALL_SIGNIN HERE
        if(success) {
            Log.i(TAG, "Signed in!")
            UserData.setSignedIn(true)
        }
        else {
            Log.i(TAG, "Error signing in.")
        }
    }

    // Set carousel images from World Countries API
    fun setFlagImages(gallery: LinearLayout) {
        // Track countries loaded into carousel
        val carousel: MutableList<Country> = mutableListOf()
        var i = 0

        // Fill carousel
        while(carousel.size < gallery.childCount) {
            // Get a random country
            val country = atlas.getRandom()
            // If not in carousel, add its flag
            if(!carousel.contains(country)) {
                carousel.add(country)
                setFlag(gallery.getChildAt(i) as ImageView, country)
                i++
            }
        }
    }

    fun setFlag(imgV: ImageView?, country: Country?) {
        if(imgV == null || country == null) {
            Log.e(TAG,"Failed to set flag image for game.")
            return
        }
        val url = "http://www.geognos.com/api/en/countries/flag/" +
                country.getCode() +
                ".png"

        Picasso.get()
            .load(url)
            .into(imgV)

        Log.i(TAG, "Flag being set for: " + country.getName())
    }

    // Autoscroll the carousel flags
    fun autoSmoothScroll(gallery: LinearLayout) {
        autoscroll = TranslateAnimation(
            TranslateAnimation.ABSOLUTE,
            0f,
            TranslateAnimation.ABSOLUTE,
            -5400f,
            TranslateAnimation.ABSOLUTE,
            0f,
            TranslateAnimation.ABSOLUTE,
            0f
        )
        (autoscroll as TranslateAnimation).duration = 80000
        (autoscroll as TranslateAnimation).repeatCount = Animation.INFINITE
        (autoscroll as TranslateAnimation).repeatMode = Animation.REVERSE
        (autoscroll as TranslateAnimation).interpolator = LinearInterpolator()

        gallery.startAnimation(autoscroll)
    }
}