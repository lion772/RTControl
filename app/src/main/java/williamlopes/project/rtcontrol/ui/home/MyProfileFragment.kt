package williamlopes.project.rtcontrol.ui.home

import android.Manifest.*
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import williamlopes.project.rtcontrol.R
import williamlopes.project.rtcontrol.databinding.FragmentMyProfileBinding
import williamlopes.project.rtcontrol.model.User
import williamlopes.project.rtcontrol.ui.viewmodel.MyProfileViewModel
import williamlopes.project.rtcontrol.util.Constants
import williamlopes.project.rtcontrol.util.empty
import java.io.IOException


class MyProfileFragment : Fragment() {
    private val viewModel: MyProfileViewModel by viewModel()
    private var selectedImageFileUri: Uri? = null
    private lateinit var databinding: FragmentMyProfileBinding
    private lateinit  var userHashMap: HashMap<String, Any>
    private var profileImageURL: Uri? = null
    private lateinit var userDetails: User
    private val contentResolver: ContentResolver? = null
    private lateinit var progressDialog: Dialog

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getActionBar()?.title = getString(R.string.user_profile)
    }

    private fun getActionBar(): androidx.appcompat.app.ActionBar? {
        return (activity as HomeActivity?)?.supportActionBar
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false)
        return databinding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUser()
        observeViewModel()
        setupListener()
        setUploadListener()
    }

    private fun setUploadListener() {

    }


    @ExperimentalCoroutinesApi
    private fun setupListener() {
        iv_profile_user_image.setOnClickListener {
            checkPermissionForExternalStorage()
        }

        btn_update.setOnClickListener {
            selectedImageFileUri?.let {
                viewModel.apply {
                    updateProfileImage(selectedImageFileUri)
                    updateUserProfileData()?.let {hashMapUser ->
                        GlobalScope.launch(Dispatchers.Main) {
                            viewModel.anyChangeVerified(hashMapUser)
                        }
                    }

                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE) {
            data?.let { DataResult ->
                selectedImageFileUri = DataResult.data
                selectedImageFileUri?.let { updateUserProfileData() }
            }
            try {
                Glide.with(this)
                    .load(selectedImageFileUri)
                    .centerCrop()
                    .transform(CircleCrop())
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(databinding.ivProfileUserImage)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun observeViewModel() {

        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                updateNavigationUserDetails(user)
                databinding.user = user
                userDetails = user
            }
        }

        viewModel.profileImage.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                Toast.makeText(
                    activity as HomeActivity,
                    getString(R.string.success_saving_image),
                    Toast.LENGTH_SHORT
                ).show()
                //(activity as HomeActivity).onBackPressed()
            } ?: run {
                Toast.makeText(
                    activity as HomeActivity,
                    getString(R.string.fail_saving_image),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.profileImageUpload.observe(viewLifecycleOwner) { uriUpload ->
            uriUpload?.let {
                updateUserProfileData()
            }
        }
    }

    private fun updateUserProfileData():HashMap<String,Any>? {
        selectedImageFileUri?.let {
            userHashMap = HashMap()
                if (selectedImageFileUri != userDetails.image.toUri()) {
                    userHashMap[Constants.IMAGE] = selectedImageFileUri.toString()
                }
                if (et_name.text.toString() != userDetails.name) {
                    userHashMap[Constants.NAME] = userDetails.name
                } else if (et_mobile.text.toString() != userDetails.mobile) {
                    userHashMap[Constants.MOBILE] = userDetails.mobile
                }
                return userHashMap
        } ?: run { return null }
    }

    private fun updateNavigationUserDetails(loggedInUser: User) {
        try {
            Glide.with(this)
                .load(loggedInUser.image)
                .centerCrop()
                .transform(CircleCrop())
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_user_place_holder)
                .into(databinding.ivProfileUserImage)


            if (!loggedInUser.mobile.isBlank()) {
                databinding.etMobile.setText(loggedInUser.mobile)
            } else {
                databinding.etMobile.setText(String.Companion.empty())
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showImageChooser() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }


    private fun checkPermissionForExternalStorage() {
        return if (Build.VERSION.SDK_INT >= SDK_VALUE) {
            val resultWrite = ContextCompat.checkSelfPermission(
                activity as HomeActivity,
                permission.WRITE_EXTERNAL_STORAGE
            )
            val resultRead = ContextCompat.checkSelfPermission(
                activity as HomeActivity,
                permission.READ_EXTERNAL_STORAGE
            )
            if (resultWrite == PackageManager.PERMISSION_GRANTED && resultRead == PackageManager.PERMISSION_GRANTED) {
                showImageChooser()
            } else {
                ActivityCompat.requestPermissions(
                    activity as HomeActivity, arrayOf(
                        permission.WRITE_EXTERNAL_STORAGE,
                        permission.READ_EXTERNAL_STORAGE
                    ),
                    REQUEST_CODE
                )
            }
        } else {
            showImageChooser()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED
        ) {

            showImageChooser()

        } else {
            Toast.makeText(
                activity as HomeActivity,
                getString(R.string.permission_denied_storage),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
        private const val REQUEST_CODE = 1
        private const val SDK_VALUE = 23
    }

    fun showProgressDialog(text: String) {
        progressDialog = Dialog(activity as HomeActivity)
        progressDialog.apply {
            setContentView(R.layout.dialog_progress)
            progressDialog.tv_progress_text.text = text
            progressDialog.show()
        }
    }

    fun hideProgressDialog() {
        progressDialog.dismiss()
    }


}