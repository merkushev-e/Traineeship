package com.testtask.traineeship.data.repositories

import com.testtask.traineeship.domain.AppState
import com.testtask.traineeship.domain.model.Contact


interface Repository<T> {
    suspend fun getData(isDataFromDB: Boolean) : List<T>
    suspend fun saveContacts(contact: List<Contact>)
    suspend fun saveContact(contact: Contact)
    suspend fun deleteContact(contact: Contact)
    suspend fun updateCurrent(number: String, name: String, lastName: String, prev_number: String)

}