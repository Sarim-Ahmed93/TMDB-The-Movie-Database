package com.example.tmdb_themoviedatabase.main.common

import java.util.concurrent.atomic.AtomicLong

object UidGenerator {
    private var idBase = AtomicLong(Long.MIN_VALUE)

    fun getNewUid(): Long {
        return idBase.getAndIncrement()
    }
}