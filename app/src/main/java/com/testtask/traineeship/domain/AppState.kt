package com.testtask.traineeship.domain

import com.testtask.traineeship.domain.model.Contact
import kotlinx.android.parcel.Parcelize


sealed class AppState{
    data class Success(val data: List<Contact>?) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class Loading(val progress: Int?) : AppState()
}
