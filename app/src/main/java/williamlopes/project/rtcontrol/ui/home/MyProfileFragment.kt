package williamlopes.project.rtcontrol.ui.home

import android.Manifest
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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import williamlopes.project.rtcontrol.R
import williamlopes.project.rtcontrol.model.User
import williamlopes.project.rtcontrol.ui.viewmodel.MyProfileViewModel
import williamlopes.project.rtcontrol.util.empty
import java.io.IOException


class MyProfileFragment : Fragment() {
    private val viewModel: MyProfileViewModel by viewModel()
    private var selectedImageFileUri: Uri? = null
    private var profileImageURL: Uri? = null
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUser()

        observeViewModel()
        setupListener()
    }


    @ExperimentalCoroutinesApi
    private fun setupListener() {
        iv_profile_user_image.setOnClickListener {
            /*if (ContextCompat.checkSelfPermission(activity as HomeActivity, permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                showImageChooser()

            } else {
                ActivityCompat.requestPermissions(activity as HomeActivity,
                    arrayOf(permission.READ_EXTERNAL_STORAGE), READ_STORAGE_PERMISSION_CODE)
            }*/
            checkPermissionForExternalStorage()
        }

        btn_update.setOnClickListener {
            selectedImageFileUri?.let {
                viewModel.updateProfileImage(selectedImageFileUri)
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            data?.let { DataResult ->
                selectedImageFileUri = DataResult.data
            }
            try {
                Glide.with(this)
                    .load(selectedImageFileUri)
                    .centerCrop()
                    .transform(CircleCrop())
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(iv_profile_user_image)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun observeViewModel() {

        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                updateNavigationUserDetails(user)
            }
        }

        viewModel.profileImage.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                Toast.makeText(activity as HomeActivity, getString(R.string.success_saving_image), Toast.LENGTH_SHORT).show()
            } ?: kotlin.run {
                Toast.makeText(activity as HomeActivity, getString(R.string.fail_saving_image), Toast.LENGTH_SHORT).show()
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

            et_name.setText(loggedInUser.name)
            et_email.setText(loggedInUser.email)
            if (!loggedInUser.mobile.isBlank()) {
                et_mobile.setText(loggedInUser.mobile)
            } else {
                et_mobile.setText(String.Companion.empty())
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
            val resultWrite = ContextCompat.checkSelfPermission(activity as HomeActivity, permission.WRITE_EXTERNAL_STORAGE)
            val resultRead = ContextCompat.checkSelfPermission(activity as HomeActivity, permission.READ_EXTERNAL_STORAGE)
            if (resultWrite == PackageManager.PERMISSION_GRANTED && resultRead == PackageManager.PERMISSION_GRANTED) {
                showImageChooser()
            } else {
                ActivityCompat.requestPermissions(activity as HomeActivity, arrayOf(
                        permission.WRITE_EXTERNAL_STORAGE,
                        permission.READ_EXTERNAL_STORAGE),
                        REQUEST_CODE
                )
            }
        } else{
            showImageChooser()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            showImageChooser()

        } else {
            Toast.makeText(context, getString(R.string.permission_denied_storage), Toast.LENGTH_SHORT).show()
        }
    }


    private fun getFileExtension(uri: Uri): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver?.getType(uri))
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