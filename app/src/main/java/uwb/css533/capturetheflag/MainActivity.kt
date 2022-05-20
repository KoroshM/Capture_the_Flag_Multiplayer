package uwb.css533.capturetheflag

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uwb.css533.capturetheflag.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), FragmentNavigation {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding       // Easily access UI elements
    private var fragment: Fragment? = null                      // Currently loaded fragment
    private val viewModel = MyViewModel()

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
            if(viewModel.auth.equals(true)) {
                button.visibility = View.GONE
                fragment = SignedInFragment(viewModel)
                replaceFragment(fragment as SignedInFragment)

            } else {
                button.visibility = View.GONE
                fragment = LoginFragment(viewModel)
                replaceFragment(fragment as LoginFragment)
            }
        }
    }

    override fun replaceFragment(frag: Fragment) {
        val fragManager = supportFragmentManager
        fragManager.beginTransaction()
            .replace(R.id.fragcontainer_main, frag)
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }

    private fun createRoom(frag: CreateRoomFragment, roomCode: String) {
        frag.getCodeDisplay()?.text = roomCode
        frag.getStartButton()?.setOnClickListener {
            // TODO: Implement start game

            // TODO: Implement activity change to game layout and logic
        }
    }

    private fun joinRoom(frag: JoinRoomFragment) {
        frag.getFoundText()?.visibility = View.INVISIBLE
        frag.getSearchButton()?.visibility = View.VISIBLE

        frag.getSearchButton()?.setOnClickListener {
            val code = frag.getCodeField()?.text
            if(code == null) {
                Toast.makeText(this, "Please enter a room code to join.", Toast.LENGTH_SHORT).show()

            } else {
                // TODO: Find room to join
                val response = "Successfully joined room."

                if(response == "Successfully joined room.") {
                    frag.getFoundText()?.visibility = View.VISIBLE
                    frag.getSearchButton()?.visibility = View.GONE
                }
            }
        }
    }
}