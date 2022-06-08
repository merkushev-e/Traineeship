package com.testtask.traineeship.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact (
    val number: String,
    val name: String,
    val lastName: String,
    val picId: Int
        ) : Parcelable