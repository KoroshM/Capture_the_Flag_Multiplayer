package uwb.css533.capturetheflag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment



class SignedInFragment(private val model: MyViewModel) : Fragment(R.layout.activity_signedin)  {

    private var btnCreate: Button? = null
    private var btnJoin: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val returnView = inflater.inflate(R.layout.activity_signedin, container, false)
        btnCreate = returnView.findViewById<Button>(R.id.frag_button_create)
        btnJoin = returnView.findViewById<Button>(R.id.frag_button_join)

        btnCreate?.setOnClickListener {
            val roomCode = createRoom(model.getUser())
            if(roomCode.startsWith("-")) {
                Toast.makeText(activity, "Unable to create room.", Toast.LENGTH_SHORT).show()

            } else {
                val navLogin = activity as FragmentNavigation
                navLogin.replaceFragment(CreateRoomFragment(model, roomCode))
            }
        }

        btnJoin?.setOnClickListener {
            val navLogin = activity as FragmentNavigation
            navLogin.replaceFragment(JoinRoomFragment(model))
        }

        return returnView
    }

    private fun createRoom(user: User?): String {
        if(user == null) {
            return "-1"
        }

        // TODO: Implement room creation
        val response = "12345"

        return response
    }
}