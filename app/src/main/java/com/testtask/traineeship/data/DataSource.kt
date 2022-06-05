package com.testtask.traineeship.data

interface DataSource<T> {
    suspend fun getData(): T
}