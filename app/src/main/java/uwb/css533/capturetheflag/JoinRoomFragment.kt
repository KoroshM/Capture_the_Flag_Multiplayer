package uwb.css533.capturetheflag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment


class JoinRoomFragment : Fragment(R.layout.join_screen)  {

    private var btnSearch: Button? = null
    private var textRoomFound: TextView? = null
    private var roomCodeField: EditText? = null

    fun JoinRoomFragment() {
        // Required empty public constructor
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val returnView = inflater.inflate(R.layout.login_screen, container, false)
        btnSearch = returnView.findViewById<Button>(R.id.frag_button_search)
        textRoomFound = returnView.findViewById<TextView>(R.id.frag_textview_found)
        roomCodeField = returnView.findViewById<EditText>(R.id.frag_edittext_sessionID)
        return returnView
    }

    fun getSearchButton(): Button? {
        return btnSearch
    }

    fun getFoundText(): TextView? {
        return textRoomFound
    }

    fun getCodeField(): EditText? {
        return roomCodeField
    }
}