package my.edu.tarc.contact.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import my.edu.tarc.contact.model.Contact

@Dao
interface ContactDao {
    // DAO = Data Access Object

    // Query (R - retrieve)
    @Query("SELECT * FROM contact")
    fun getAllContact():LiveData<List<Contact>>

    @Query("SELECT * FROM contact WHERE name LIKE :name")
    fun findByName(name: String): List<Contact>

    @Query("SELECT * FROM contact WHERE phone = :phone")
    fun findByPhone(phone: String): Contact

    // Data Manipulation function (CUD - create, update, delete)
    @Insert
    suspend fun insert(contact: Contact)

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)
}