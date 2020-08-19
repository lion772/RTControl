package williamlopes.project.rtcontrol.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
class UserModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Int = 0

    @ColumnInfo(name = "email")
    var email:String = ""

    @ColumnInfo(name = "password")
    var password: String = ""
}