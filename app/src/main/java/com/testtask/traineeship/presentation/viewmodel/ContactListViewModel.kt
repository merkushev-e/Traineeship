package com.testtask.traineeship.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testtask.traineeship.domain.AppState
import com.testtask.traineeship.domain.Interactor
import com.testtask.traineeship.domain.MainInteractor
import com.testtask.traineeship.domain.model.Contact
import kotlinx.coroutines.*

class ContactListViewModel(
    private val interactor: MainInteractor,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    val liveData: LiveData<AppState> = liveDataToObserve

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        }


    fun getData(isDataFromDB: Boolean) {
        liveDataToObserve.value = AppState.Loading(null)

        viewModelScope.launch(defaultDispatcher + coroutineExceptionHandler) {
            liveDataToObserve.postValue(interactor.getData(isDataFromDB))
        }
    }


    fun saveContactToDB(contact: Contact) {
        viewModelScope.launch(defaultDispatcher + coroutineExceptionHandler) {
            interactor.saveContact(contact)
        }
    }

    fun saveContactsToDBAndShow(contactList: List<Contact>) {
        viewModelScope.launch(defaultDispatcher + coroutineExceptionHandler) {
           val job = launch {
                interactor.saveContacts(contactList)
            }
            job.join()
            launch { liveDataToObserve.postValue(interactor.getData(true))}
        }
    }

    fun deleteContactFromDb(contact: Contact) {
        viewModelScope.launch(defaultDispatcher + coroutineExceptionHandler) {
            interactor.deleteContact(contact)
        }
    }


    fun updateContactInDb(
        number: String,
        name: String,
        lastName: String,
        prev_number: String
    ){
        viewModelScope.launch(defaultDispatcher + coroutineExceptionHandler) {
            interactor.updateCurrent(number,name,lastName,prev_number)
        }
    }

    private fun handleError(error: Throwable) {
        liveDataToObserve.postValue(AppState.Error(error))
    }

    override fun onCleared() {
        liveDataToObserve.value = AppState.Success(null)
    }


}