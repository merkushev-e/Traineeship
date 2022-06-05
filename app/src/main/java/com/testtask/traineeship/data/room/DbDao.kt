package com.testtask.traineeship.data.room

import androidx.room.*

@Dao
interface DbDao {

    @Query("SELECT * FROM ContactEntity")
    suspend fun all(): List<ContactEntity>

    @Query("SELECT * FROM ContactEntity WHERE number LIKE :number")
    suspend fun getDataByWord(number: String): ContactEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: ContactEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entities: List<ContactEntity>)
    @Update
    suspend fun update(entity: ContactEntity)

    @Delete
    suspend fun delete(entity: ContactEntity)

    @Query("UPDATE ContactEntity SET number = :number, name = :name, lastName = :lastName WHERE number = :prev_number")
    fun updateCurrent(number: String, name: String, lastName: String, prev_number: String )
}