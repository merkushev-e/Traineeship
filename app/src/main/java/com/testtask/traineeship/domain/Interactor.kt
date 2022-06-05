package com.testtask.traineeship.domain

import com.testtask.traineeship.domain.model.Contact

interface Interactor<T> {
    suspend fun getData(fromRemoteSource: Boolean): T
    suspend fun saveContacts(contactList: List<Contact>)
    suspend fun saveContact(contact: Contact)
    suspend fun deleteContact(contact: Contact)
    suspend fun updateCurrent(number: String, name: String, lastName: String, prev_number: String)
}