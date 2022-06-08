package com.testtask.traineeship.data.repositories

import com.testtask.traineeship.data.ContactsList
import com.testtask.traineeship.data.room.ContactEntity
import com.testtask.traineeship.data.room.DataSourceLocal
import com.testtask.traineeship.domain.AppState
import com.testtask.traineeship.domain.model.Contact
import com.testtask.traineeship.utils.mapContactEntityToContact
import com.testtask.traineeship.utils.mapContactListToContactEntityList
import com.testtask.traineeship.utils.mapContactToContactEntity

class RepositoryImpl(
    private val localDataSource: DataSourceLocal<List<ContactEntity>>
) : Repository<Contact> {




    private suspend fun getDataFromDB(): List<Contact> {
        return mapContactEntityToContact(localDataSource.getData())
    }

    override suspend fun getData(isDataFromDB: Boolean): List<Contact> {
        return if (isDataFromDB) {
            getDataFromDB()
        } else {
            TODO()
            //если будут данные из другого источника
        }
    }

    override suspend fun saveContacts(contact: List<Contact>) {
      localDataSource.saveAllToDb(mapContactListToContactEntityList(contact))
    }

    override suspend fun saveContact(contact: Contact) {
        localDataSource.addContact(mapContactToContactEntity(contact))
    }

    override suspend fun deleteContact(contact: Contact) {
        localDataSource.deleteContact(mapContactToContactEntity(contact))
    }

    override suspend fun updateCurrent(
        number: String,
        name: String,
        lastName: String,
        prev_number: String
    ) {
        localDataSource.updateCurrent(number,name,lastName,prev_number)
    }
}
