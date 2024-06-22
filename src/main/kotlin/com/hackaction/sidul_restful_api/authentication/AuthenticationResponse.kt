package com.hackaction.sidul_restful_api.authentication

import com.hackaction.sidul_restful_api.core.enums.ResponseStatus
import com.hackaction.sidul_restful_api.core.utils.Response

data class AuthenticationResponse(
    override val status: ResponseStatus, override val data: Map<String, Any>
) : Response(status, data)