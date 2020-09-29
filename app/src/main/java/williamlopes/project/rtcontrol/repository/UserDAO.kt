package williamlopes.project.rtcontrol.repository

import androidx.room.*
import williamlopes.project.rtcontrol.model.User

@Dao
interface UserDAO {
    @Insert fun save(user: User): Long
    @Update fun update(user: User): Int
    @Delete fun delete(user: User): Int
    @Query("SELECT * FROM User WHERE email = :email AND password = :password")
    fun getUser(email:String, password:String): User


}