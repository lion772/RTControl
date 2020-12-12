package williamlopes.project.rtcontrol.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_list.*
import williamlopes.project.rtcontrol.R

class ListFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getActionBar()?.title = getString(R.string.opening_list)
        getActionBar()?.setBackgroundDrawable(ResourcesCompat.getDrawable((resources), R.color.colorPrimary, null))
        val window: Window = (activity?.window) as Window
        window.statusBarColor = ContextCompat.getColor(activity as HomeActivity, R.color.colorPrimaryDark)

        fab.setOnClickListener {
            startActivity(Intent(activity, DetailActivity::class.java))
        }

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


}