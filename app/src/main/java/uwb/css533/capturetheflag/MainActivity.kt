package uwb.css533.capturetheflag

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import uwb.css533.capturetheflag.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding       // Easily access UI elements

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)                      // Required setup
        binding = ActivityMainBinding.inflate(layoutInflater)   // Required for binding
        setContentView(binding.root)                            // Required for binding

        Log.i(TAG, "Beginning activity.")
        Backend.initialize()
        Backend.setFlagImages(binding.linearlayoutGallery)         // Method to get images for flag carousel
        Backend.autoSmoothScroll(binding.linearlayoutGallery)      // Method to slowly scroll the carousel

        val button = binding.buttonLogin
        button.setOnClickListener {
            button.visibility = View.GONE
            replaceFragment(LoginFragment())
        }
    }

    private fun replaceFragment(frag: Fragment) {
        val fragManager = supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.fragcontainer_main, frag)
        fragTransaction.commit()
    }
}