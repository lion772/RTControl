package williamlopes.project.rtcontrol.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_list.*
import williamlopes.project.rtcontrol.R

class ListFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getActionBar()?.title = getString(R.string.qrcodes_list)
        getActionBar()?.setBackgroundDrawable(resources.getDrawable((R.color.colorPrimary)))
        val window: Window = (activity?.window) as Window
        window.statusBarColor = ContextCompat.getColor(activity as HomeActivity, R.color.colorPrimaryDark)

    }

    private fun getActionBar(): androidx.appcompat.app.ActionBar? {
        return (activity as HomeActivity?)?.supportActionBar
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToDetailFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

}