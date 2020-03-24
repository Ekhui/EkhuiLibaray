package com.example.ekhuibaselibrary.api.exception

data class ApiException(
        var errorCode: Int,
        var errorMessage: String
) : Exception()