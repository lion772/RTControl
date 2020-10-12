package williamlopes.project.rtcontrol.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import williamlopes.project.rtcontrol.util.empty

@Entity(tableName = "User")
@Parcelize
data class User(
    var id: String = String.Companion.empty(),
    val name: String = String.Companion.empty(),
    var email: String = String.Companion.empty(),
    val image: String = String.Companion.empty(),
    val mobile: String = String.Companion.empty(),
    val fcmToken: String = String.Companion.empty()
) : Parcelable

