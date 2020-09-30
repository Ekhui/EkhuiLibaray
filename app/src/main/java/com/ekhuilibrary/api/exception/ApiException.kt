package com.ekhuilibrary.api.exception

data class ApiException(
        var errorCode: Int,
        var errorMessage: String
) : Exception()