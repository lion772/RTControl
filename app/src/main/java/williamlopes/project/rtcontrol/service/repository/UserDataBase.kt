package williamlopes.project.rtcontrol.service.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import williamlopes.project.rtcontrol.service.model.UserModel

@Database(entities = [UserModel::class], version = 1)
abstract class UserDataBase: RoomDatabase() {
    abstract fun userDao(): UserDAO

    companion object{
        private lateinit var INSTANCE: UserDataBase
        fun getDatabase(context: Context): UserDataBase{
            if(!::INSTANCE.isInitialized){
                synchronized(UserDataBase::class){
                    INSTANCE = Room.databaseBuilder(context, UserDataBase::class.java, "UserDB")
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }

    }
}