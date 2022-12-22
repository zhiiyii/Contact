package my.edu.tarc.contact.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import my.edu.tarc.contact.dao.ContactDao
import my.edu.tarc.contact.model.Contact

class ContactRepository(val contactDao: ContactDao) {
    // Create a cache copy of data in the DAO
    val allContact: LiveData<List<Contact>> = contactDao.getAllContact()

    @WorkerThread
    suspend fun insert(contact: Contact) { // launch suspend function, only in coroutine
        contactDao.insert(contact)
    }

    @WorkerThread
    suspend fun delete(contact: Contact) {
        contactDao.delete(contact)
    }

    @WorkerThread
    suspend fun update(contact: Contact) {
        contactDao.update(contact)
    }

    fun findByName(name: String): List<Contact> {
        return contactDao.findByName(name)
    }

    fun findByPhone(phone: String): Contact {
        return contactDao.findByPhone(phone)
    }
}