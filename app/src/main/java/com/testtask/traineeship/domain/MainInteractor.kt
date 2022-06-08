package com.testtask.traineeship.domain

import com.testtask.traineeship.data.ContactsList.createNContacts
import com.testtask.traineeship.data.repositories.Repository
import com.testtask.traineeship.domain.model.Contact

class MainInteractor(
    private val repository: Repository<Contact>,
    ) : Interactor<AppState> {
    override suspend fun getData(isDataFromDB: Boolean): AppState {
        val appState: AppState
            appState = AppState.Success(repository.getData(isDataFromDB))
        return appState
    }

    override suspend fun saveContacts(contactList: List<Contact>) {
        repository.saveContacts(contactList)
    }

    override suspend fun saveContact(contact: Contact) {
        repository.saveContact(contact)
    }

    override suspend fun deleteContact(contact: Contact) {
        repository.deleteContact(contact)
    }

    override suspend fun updateCurrent(
        number: String,
        name: String,
        lastName: String,
        prev_number: String
    ) {
        repository.updateCurrent(number, name, lastName, prev_number)
    }

}