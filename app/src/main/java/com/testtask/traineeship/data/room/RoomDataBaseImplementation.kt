package com.testtask.traineeship.data.room


class RoomDataBaseImplementation(private val dbDao: DbDao) : DataSourceLocal<List<ContactEntity>> {
    override suspend fun getData(): List<ContactEntity> {
        return dbDao.all()
    }


    override suspend fun saveAllToDb(dataList: List<ContactEntity>) {
        dbDao.insertAll(dataList)
    }

    override suspend fun addContact(contact: ContactEntity) {
        dbDao.insert(contact)
    }

    override suspend fun deleteContact(contact: ContactEntity) {
        dbDao.delete(contact)
    }

    override suspend fun updateCurrent(
        number: String,
        name: String,
        lastName: String,
        prev_number: String
    ) {
        dbDao.updateCurrent(number, name, lastName, prev_number)
    }


}
