package williamlopes.project.rtcontrol.ui.home

import android.Manifest.*
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import williamlopes.project.rtcontrol.R
import williamlopes.project.rtcontrol.model.User
import williamlopes.project.rtcontrol.ui.viewmodel.MyProfileViewModel
import williamlopes.project.rtcontrol.util.empty
import java.io.IOException
import java.util.jar.Manifest


class MyProfileFragment : Fragment() {
    private val viewModel: MyProfileViewModel by viewModel()
    private var selectecImageFileUri:Uri? = null

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getActionBar()?.title = getString(R.string.user_profile)
    }

    private fun getActionBar(): androidx.appcompat.app.ActionBar? {
        return (activity as HomeActivity?)?.supportActionBar
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUser()
        observeViewModel()
        setProfileImage()
    }

    private fun setProfileImage() {
        iv_profile_user_image.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    activity as HomeActivity,
                    permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                showImageChooser()

            } else {
                ActivityCompat.requestPermissions(
                    activity as HomeActivity,
                    arrayOf(permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }
    }

    private fun showImageChooser(){
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            showImageChooser()

        } else {
            Toast.makeText(activity as HomeActivity, getString(R.string.permission_denied_storage), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE){
            data?.let {DataResult ->
                selectecImageFileUri = DataResult.data
            }
            try {
                Glide.with(this)
                    .load(selectecImageFileUri)
                    .centerCrop()
                    .transform(CircleCrop())
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(iv_profile_user_image)
            }catch (e:IOException){
                e.printStackTrace()
            }

        }
    }

    private fun observeViewModel() {

        viewModel.user.observe(viewLifecycleOwner) {user ->
            user?.let {
                updateNavigationUserDetails(user)
            }
        }
    }

    private fun updateNavigationUserDetails(loggedInUser: User) {
        try {
            Glide.with(this)
                .load(loggedInUser.image)
                .centerCrop()
                .transform(CircleCrop())
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_user_place_holder)
                .into(iv_profile_user_image)

            et_name.setText( loggedInUser.name )
            et_email.setText(loggedInUser.email)
            if (!loggedInUser.mobile.isBlank()){
                et_mobile.setText(loggedInUser.mobile)
            } else {
                et_mobile.setText(String.Companion.empty())
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object{
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }


}