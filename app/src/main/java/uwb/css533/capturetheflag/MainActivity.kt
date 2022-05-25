package uwb.css533.capturetheflag

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import uwb.css533.capturetheflag.databinding.ActivityMainBinding

// First and main class called on app start. Navigates to LoginFragment
class MainActivity : AppCompatActivity(), FragmentNavigation {

    // Change these to your configured server's address and port
    private val IP = "10.0.2.2"
    private val PORT = 8080
    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding           // Easily access UI elements
    private val viewModel = MyViewModel(IP, PORT)

    // Main method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)                      // Required setup
        binding = ActivityMainBinding.inflate(layoutInflater)   // Required for binding
        setContentView(binding.root)                            // Required for binding

        Log.i(TAG, "Beginning main activity.")
        Backend.initialize()
        Backend.setFlagImages(binding.linearlayoutGallery)      // Method to get images for flag carousel
        Backend.autoSmoothScroll(binding.linearlayoutGallery)   // Method to slowly scroll the carousel

        val button = binding.buttonLogin
        button.setOnClickListener {
            if(viewModel.auth.equals(true)) {
                button.visibility = View.GONE
                replaceFragment(SignedInFragment(viewModel))

            } else {
                button.visibility = View.GONE
                replaceFragment(LoginFragment(viewModel))
            }
        }
    }

    // Used by every fragment for fragment navigation
    override fun replaceFragment(frag: Fragment) {
        val fragManager = supportFragmentManager
        fragManager.beginTransaction()
            .replace(R.id.fragcontainer_main, frag)
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }
}