package com.example.tmdb_themoviedatabase.main.backend

import com.example.tmdb_themoviedatabase.main.common.UidGenerator
import com.example.tmdb_themoviedatabase.main.backend.AppModel
/**
 * This is a wrapper for retrofit call used by the BackendModel.
 * It is used to prevent pollution of BackendModel-.users with retrofit.
 * BackendModel should not expose any internal dependencies to it's users.
 */
class AsyncBackendOperationHandle {
    val uId by lazy { UidGenerator.getNewUid() }

    fun cancel() {
        AppModel.backendModel.value?.cancelAsyncOperation(this)
    }
}