package my.edu.tarc.contact.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import my.edu.tarc.contact.dao.ContactDao
import my.edu.tarc.contact.model.Contact

@Database(entities = arrayOf(Contact::class), version = 1, exportSchema = false)
abstract class ContactDatabase: RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        // Singleton prevents multiple instance of the database opening at the same time
        @Volatile // can be destroyed, uninstall app, data destroyed
        private var INSTANCE: ContactDatabase? = null

        fun getDatabase(context: Context): ContactDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) { // database exist
                return tempInstance
            }

            // Create an instance of the database
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDatabase::class.java,
                    "contact_db"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}