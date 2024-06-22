package com.hackaction.sidul_restful_api.core.enums

enum class ResponseStatus {
    // Type SUCCESS -> All went well, and (usually) some data was returned
    // Type FAIL -> There was a problem with the data submitted, or some pre-condition of the API call wasn't satisfied
    // Type ERROR -> An error occurred in processing the request, i.e. an exception was thrown
    SUCCESS, FAIL, ERROR
}