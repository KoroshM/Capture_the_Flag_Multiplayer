package uwb.css533.capturetheflag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment


class LoginFragment : Fragment(R.layout.login_screen)  {

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
        return returnView
    }

    fun getButton(): Button? {
        return btnLogin
    }

    fun getUserField(): EditText? {
        return userField
    }

    fun getPassField(): EditText? {
        return passField
    }
}