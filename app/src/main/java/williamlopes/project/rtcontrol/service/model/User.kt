package williamlopes.project.rtcontrol.service.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id:Int,
    var name:String,
    var email:String,
    var password:String
):Parcelable