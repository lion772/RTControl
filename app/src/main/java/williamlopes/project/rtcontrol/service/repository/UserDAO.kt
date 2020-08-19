package williamlopes.project.rtcontrol.service.repository

import androidx.room.*
import williamlopes.project.rtcontrol.service.model.UserModel

@Dao
interface UserDAO {
    @Insert fun save(user: UserModel): Long
    @Update fun update(user: UserModel): Int
    @Delete fun delete(user: UserModel): Int
    @Query("SELECT * FROM User WHERE email = :email AND password = :password")
    fun getUser(email:String, password:String): UserModel


}