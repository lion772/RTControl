package williamlopes.project.rtcontrol.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_list.*
import williamlopes.project.rtcontrol.R

class ListFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getActionBar()?.title = getString(R.string.qrcodes_list)

        //setupActionBar()
    }

    private fun getActionBar(): androidx.appcompat.app.ActionBar? {
        return (activity as HomeActivity?)?.supportActionBar
    }

    private fun setupActionBar(){
        //(activity as HomeActivity?)?.setSupportActionBar(toolbar_fragment_list)
        //toolbar_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        /*toolbar_activity.setNavigationOnClickListener{
            tooggleDrawer()
        }*/
    }

    /*private fun tooggleDrawer(){
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }*/

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