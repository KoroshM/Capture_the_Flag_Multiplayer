package uwb.css533.capturetheflag

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import uwb.css533.capturetheflag.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding       // Easily access UI elements
    private var fragment: Fragment? = null                      // Currently loaded fragment

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
            fragment = LoginFragment()
            replaceFragment(fragment as LoginFragment)
            login(fragment as LoginFragment)
        }
    }

    private fun replaceFragment(frag: Fragment) {
        val fragManager = supportFragmentManager
        fragManager.beginTransaction()
            .replace(R.id.fragcontainer_main, frag)
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }

    private fun login(frag: LoginFragment) {
        var userField = frag.getUserField()
        var passField = frag.getPassField()
        frag.getButton()?.setOnClickListener {
            val userText = userField?.text
            val passText = passField?.text

            if(userText == null || passText == null) {
                Toast.makeText(this,"Please enter a username and password.",Toast.LENGTH_SHORT).show()
            }

            // TODO: Implement login functionality
            val response = "Successfully logged in."

            if(response == "Successfully logged in.") {
                fragment = SignedInFragment()
                replaceFragment(fragment as SignedInFragment)
                signedIn(fragment as SignedInFragment)
            }
        }
    }

    private fun signedIn(frag: SignedInFragment) {
        frag.getCreate()?.setOnClickListener {
            // TODO: Implement room creation
            val response = "12345"
            if (Integer.parseInt(response) > 0) {
                fragment = CreateRoomFragment()
                replaceFragment(fragment as CreateRoomFragment)
                createRoom(fragment as CreateRoomFragment, response)

            } else { // Response was -1, error
                Toast.makeText(this,"Unable to create room.",Toast.LENGTH_SHORT).show()
            }
        }

        frag.getJoin()?.setOnClickListener {
            // TODO: Implement join room

            fragment = JoinRoomFragment()
            replaceFragment(fragment as JoinRoomFragment)
            joinRoom(fragment as JoinRoomFragment)
        }
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