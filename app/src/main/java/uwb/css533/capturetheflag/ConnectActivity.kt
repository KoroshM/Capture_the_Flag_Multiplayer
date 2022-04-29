package uwb.css533.capturetheflag

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import uwb.css533.capturetheflag.databinding.ActivitySignedinBinding

class ConnectActivity : AppCompatActivity() {
    private val TAG = "ConnectActivity"
    private lateinit var binding: ActivitySignedinBinding       // Easily access UI elements

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)                      // Required setup
        binding = ActivitySignedinBinding.inflate(layoutInflater)   // Required for binding
        setContentView(binding.root)                            // Required for binding

        Log.i(TAG, "Beginning activity.")
        // Establish connection

        // Set button listener for login

    }
}