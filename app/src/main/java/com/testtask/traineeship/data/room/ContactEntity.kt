package com.testtask.traineeship.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactEntity(
    @PrimaryKey
    val number: String,
    val name: String,
    val lastName: String
)