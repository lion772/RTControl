package williamlopes.project.rtcontrol.helper

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import williamlopes.project.rtcontrol.R
import williamlopes.project.rtcontrol.model.User
import williamlopes.project.rtcontrol.util.Constants
import williamlopes.project.rtcontrol.util.await
import williamlopes.project.rtcontrol.ui.home.SignInActivity
import williamlopes.project.rtcontrol.ui.home.SignUpActivity

object ConfiguracaoFirebase {
    private var referenciaFirebase: DatabaseReference? = null
    private var autenticacao: FirebaseAuth? = null
    private var storage: StorageReference? = null
    private var firebaseStore: FirebaseFirestore? = null


    //Retorna a instância do FirebaseDatabase
    val firebase: DatabaseReference?
        get() {
            if (referenciaFirebase == null) {
                referenciaFirebase = FirebaseDatabase.getInstance().reference
            }
            return referenciaFirebase
        }

    val firebaseAutenticacao: FirebaseAuth?
        get() {
            if (autenticacao == null) {
                autenticacao = FirebaseAuth.getInstance()
            }
            return autenticacao
        }

    val firebaseStorage: StorageReference?
        get() {
            if (storage == null) {
                storage = FirebaseStorage.getInstance().reference
            }
            return storage
        }

    val fireStore: FirebaseFirestore?
        get() {
            if (firebaseStore == null) {
                firebaseStore = FirebaseFirestore.getInstance()
            }
            return firebaseStore
        }

    fun registerUserIntoFirebase(activity: SignUpActivity, userInfo: User) {
        autenticacao?.currentUser?.uid?.let { Uid ->
            fireStore?.collection(Constants.USERS)
                ?.document(Uid)
                ?.set(userInfo, SetOptions.merge())
                ?.addOnSuccessListener {
                    activity.userRegisteredSuccess()
                }?.addOnFailureListener { e ->
                    Toast.makeText(
                        activity.applicationContext,
                        "${activity.applicationContext.getString(R.string.fail_signup_user_firebasestore)}: $e",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
    }

    fun signInUserIntoFirebase(activity: SignInActivity) {
        autenticacao?.currentUser?.uid?.let { Uid ->
            fireStore?.collection(Constants.USERS)
                ?.document(Uid)
                ?.get()
                ?.addOnSuccessListener { document ->
                    val loggedInUser = document.toObject(User::class.java)
                    loggedInUser?.let { activity.signInSuccess(it) }


                }?.addOnFailureListener { e ->
                    Toast.makeText(
                        activity.applicationContext,
                        "${activity.applicationContext.getString(R.string.fail_signup_user_firebasestore)}: $e",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }


    @ExperimentalCoroutinesApi
    suspend fun signInUserIntoFirebaseTeste(activity: SignInActivity) {
        try {
            autenticacao?.currentUser?.uid?.let { Uid ->
                val documentSnapshot = fireStore?.collection(Constants.USERS)
                    ?.document(Uid)
                    ?.get()?.await()
                val loggedInUser = documentSnapshot?.toObject(User::class.java)
                loggedInUser?.let { activity.signInSuccess(loggedInUser = it) }

            }
        }catch (e: java.lang.Exception){
            null
        }
    }


}