package williamlopes.project.rtcontrol.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "User")
@Parcelize
data class User(
    var id: String,
    val name: String,
    var email: String?,
    val image: String = "",
    val mobile: Long = 0,
    val fcmToken: String = ""
): Parcelable {

    fun User(){}
}