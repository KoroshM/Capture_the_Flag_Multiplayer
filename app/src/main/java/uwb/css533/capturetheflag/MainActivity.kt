package uwb.css533.capturetheflag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import uwb.css533.capturetheflag.databinding.ActivityMainBinding


const val CAROUSEL_FLAGS = 20

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding  // Easily access UI elements
    private val countries = Countries()             // Class with pre-made list of countries
    var autoscroll: Animation? = null               // Animator for flag carousel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)                      // Required setup
        binding = ActivityMainBinding.inflate(layoutInflater)   // Required for binding
        setContentView(binding.root)                            // Required for binding

        Log.i("Main", "Beginning main activity.")
        setFlagImages()         // Method to get images for flag carousel
        autoSmoothScroll()      // Method to slowly scroll the carousel
    }

    // Set carousel images from World Countries API
    private fun setFlagImages() {
        // Track countries loaded into carousel
        val carousel: MutableList<Pair<String,String>> = mutableListOf<Pair<String,String>>()
        val gallery = binding.includeMain.ImgGallery
        var i = 0

        // Fill carousel
        while(carousel.size < gallery.childCount) {
            // Get a random country
            val country = countries.getCountry()
            // If not in carousel, add its flag
            if(!carousel.contains(country)) {
                carousel.add(country)
                val url = "http://www.geognos.com/api/en/countries/flag/" +
                        country.first +
                        ".png"

                Picasso.get().
                    load(url).
                    into(gallery.getChildAt(i) as ImageView)
                i++
                Log.i("Carousel", "Flag being set for: " + country.second)
            }
        }
    }

    // Autoscroll the carousel flags
    private fun autoSmoothScroll() {
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

        binding.includeMain.ImgGallery.startAnimation(autoscroll)
    }
}