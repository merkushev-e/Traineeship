package com.testtask.traineeship.data

import com.testtask.traineeship.domain.model.Contact
import java.util.*

object ContactsList {

    private const val minRandom = 111111111
    private const val maxRandom = 999999999
    fun createNContacts(n:Int) : List<Contact>{
        var contactList = mutableListOf<Contact>()
        for ( i in 1 .. n){
            val num =  Random().nextInt(((maxRandom - minRandom) + 1) + minRandom)
            contactList.add(Contact(num.toString(),"Name", "LastName",i))
        }
        return contactList
    }
}