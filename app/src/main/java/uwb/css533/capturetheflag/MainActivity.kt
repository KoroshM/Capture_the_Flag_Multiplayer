package uwb.css533.capturetheflag

import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import uwb.css533.capturetheflag.databinding.ActivityMainBinding


const val CAROUSEL_FLAGS = 20

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding   // Easily access UI elements
    private val countries = Countries()         // Class with pre-made list of countries
    var _translateAnimation: Animation? = null  // Animator for flag scroller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)                      // Required setup
        binding = ActivityMainBinding.inflate(layoutInflater)   // Required for binding
        setContentView(binding.root)                            // Required for binding

        Log.i("Main", "Beginning main activity.")
        setFlagImages()         // Method to get images for flag carousel
        autoSmoothScroll()      // Scroller method for carousel
    }

    private fun setFlagImages() {
        val carousel: MutableList<Pair<String,String>> = mutableListOf<Pair<String,String>>()
        val gallery = binding.ImgGallery
        var i = 0

        while(carousel.size < (CAROUSEL_FLAGS - 1)) {
            val country = countries.getCountry()
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

    private fun autoSmoothScroll() {
        _translateAnimation = TranslateAnimation(
            TranslateAnimation.ABSOLUTE,
            0f,
            TranslateAnimation.ABSOLUTE,
            -300f,
            TranslateAnimation.ABSOLUTE,
            0f,
            TranslateAnimation.ABSOLUTE,
            0f
        )
        (_translateAnimation as TranslateAnimation).duration = 8000
        (_translateAnimation as TranslateAnimation).repeatCount = Animation.INFINITE
        (_translateAnimation as TranslateAnimation).repeatMode = Animation.REVERSE
        // REVERSE,RESTART,INFINITE

        (_translateAnimation as TranslateAnimation).interpolator = LinearInterpolator()
        val img = binding.ImgGallery
        img.startAnimation(_translateAnimation)
    }
}