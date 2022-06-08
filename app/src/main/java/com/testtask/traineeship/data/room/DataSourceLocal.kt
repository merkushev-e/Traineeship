package com.testtask.traineeship.data.room

import com.testtask.traineeship.data.DataSource
import com.testtask.traineeship.domain.model.Contact


interface DataSourceLocal<T>: DataSource<T> {
    suspend fun saveAllToDb(dataList: List<ContactEntity>)
    suspend fun addContact(contact: ContactEntity)
    suspend fun deleteContact(contact: ContactEntity)
    suspend fun updateCurrent(number: String, name: String, lastName: String, prev_number: String)
}