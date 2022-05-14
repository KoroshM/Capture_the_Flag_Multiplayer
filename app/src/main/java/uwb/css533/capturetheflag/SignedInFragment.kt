package uwb.css533.capturetheflag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment


class SignedInFragment : Fragment(R.layout.activity_signedin)  {

    private var btnCreate: Button? = null
    private var btnJoin: Button? = null

    fun SignedInFragment() {
        // Required empty public constructor
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val returnView = inflater.inflate(R.layout.login_screen, container, false)
        btnCreate = returnView.findViewById<Button>(R.id.frag_button_create)
        btnJoin = returnView.findViewById<Button>(R.id.frag_button_join)
        return returnView
    }

    fun getCreate(): Button? {
        return btnCreate
    }

    fun getJoin(): Button? {
        return btnJoin
    }
}