package my.edu.tarc.contact.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.edu.tarc.contact.dao.ContactDao
import my.edu.tarc.contact.database.ContactDatabase
import my.edu.tarc.contact.model.Contact
import my.edu.tarc.contact.repository.ContactRepository

class ContactViewModel(application: Application): AndroidViewModel(application) {
    // define private data
    private val _contactList = MutableLiveData<List<Contact>>()
    // define global data - expose to other classes
    var contactList: LiveData<List<Contact>> = _contactList

    private val contactRepository: ContactRepository

    init {
        // Initialize DAO
        val contactDao = ContactDatabase.getDatabase(application).contactDao()
        // Associate DAO to Repository
        contactRepository = ContactRepository(contactDao)
        // Get a copy of contact list from the repository
        contactList = contactRepository.allContact
    }

    // global scope can run from activity, view model scope is for coroutine
    fun insert(contact: Contact) = viewModelScope.launch {
        contactRepository.insert(contact)
    }

    fun delete(contact: Contact) = viewModelScope.launch {
        contactRepository.delete(contact)
    }

    fun update(contact: Contact) = viewModelScope.launch {
        contactRepository.update(contact)
    }

    fun findByName(name: String): List<Contact> {
        return contactRepository.findByName(name)
    }

    fun findByPhone(phone: String): Contact {
        return contactRepository.findByPhone(phone)
    }
}