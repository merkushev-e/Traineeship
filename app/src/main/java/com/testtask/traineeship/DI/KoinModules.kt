package com.testtask.traineeship.DI

import androidx.room.Room
import com.testtask.traineeship.data.repositories.Repository
import com.testtask.traineeship.data.repositories.RepositoryImpl
import com.testtask.traineeship.data.room.DataBase
import com.testtask.traineeship.data.room.RoomDataBaseImplementation
import com.testtask.traineeship.domain.MainInteractor
import com.testtask.traineeship.domain.model.Contact
import com.testtask.traineeship.presentation.viewmodel.ContactListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val application = module {
    single { Dispatchers.IO }
    single { Room.databaseBuilder(get(), DataBase::class.java, "ContactsDB").build() }
    single { get<DataBase>().dbDao() }
    single<Repository<Contact>> {
        RepositoryImpl(RoomDataBaseImplementation(get()))
    }
}

val mainScreen = module {
    factory { MainInteractor(get()) }
    factory { ContactListViewModel(get(), get()) }
}
