package com.testtask.traineeship.utils

import com.testtask.traineeship.data.room.ContactEntity
import com.testtask.traineeship.domain.model.Contact


fun mapContactEntityToContact(list: List<ContactEntity>): List<Contact> {
    return list.map { ContactEntity ->
        Contact(
            ContactEntity.number,
            ContactEntity.name,
            ContactEntity.lastName
        )
    }
}

fun mapContactListToContactEntityList(contactList: List<Contact>): List<ContactEntity> {
    return contactList.map { Contact ->
        ContactEntity(
            Contact.number,
            Contact.name,
            Contact.lastName,
        )
    }
}

fun mapContactToContactEntity(contact: Contact): ContactEntity {
    return ContactEntity(
        contact.number,
        contact.name,
        contact.lastName,
    )
}

