package uwb.css533.capturetheflag

import androidx.fragment.app.Fragment

// Interface to allow replaceFragment to only be implemented in MainActivity
interface FragmentNavigation {
    fun replaceFragment(frag: Fragment)
}