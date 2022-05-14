package uwb.css533.capturetheflag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment


class CreateRoomFragment : Fragment(R.layout.create_screen)  {

    private var textCodeDisplay: TextView? = null
    private var btnStart: Button? = null

    fun CreateRoomFragment() {
        // Required empty public constructor
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val returnView = inflater.inflate(R.layout.login_screen, container, false)
        textCodeDisplay = returnView.findViewById<Button>(R.id.frag_textview_sessionID_display)
        btnStart = returnView.findViewById<Button>(R.id.frag_button_start)
        return returnView
    }

    fun getCodeDisplay(): TextView? {
        return textCodeDisplay
    }

    fun getStartButton(): Button? {
        return btnStart
    }
}