package uwb.css533.capturetheflag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment


class LoginFragment(private val model: MyViewModel) : Fragment()  {

    private var userField: EditText? = null
    private var passField: EditText? = null
    private var btnLogin: Button? = null

    fun LoginFragment() {
        // Required empty public constructor
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val returnView = inflater.inflate(R.layout.login_screen, container, false)
        userField = returnView.findViewById<EditText>(R.id.frag_edittext_username)
        passField = returnView.findViewById<EditText>(R.id.frag_edittext_password)
        btnLogin = returnView.findViewById<Button>(R.id.frag_button_login)

        btnLogin?.setOnClickListener {
            model.setSignedIn(login())

            if(model.auth.equals(false)){
                Toast.makeText(activity, "Login failed.", Toast.LENGTH_SHORT).show()
            } else {
                val navLogin = activity as FragmentNavigation
                navLogin.replaceFragment(SignedInFragment(model))
            }
        }

        return returnView
    }

    private fun login(): Boolean {
        val userText = userField?.text
        val passText = passField?.text

        if(userText == null || passText == null) {
            Toast.makeText(activity,"Please enter a username and password.",Toast.LENGTH_SHORT).show()
        }

        // TODO: Implement login functionality
        val response = "Successfully logged in."

        return response == "Successfully logged in."
    }
}