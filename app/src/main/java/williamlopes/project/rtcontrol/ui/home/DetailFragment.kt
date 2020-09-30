package williamlopes.project.rtcontrol.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import williamlopes.project.rtcontrol.R

class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getActionBar()?.title = getString(R.string.user_details)
    }


    private fun getActionBar(): androidx.appcompat.app.ActionBar? {
        return (activity as HomeActivity?)?.supportActionBar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}