package com.hackaction.sidul_restful_api.core.utils

import com.hackaction.sidul_restful_api.core.enums.ResponseStatus

open class Response(open val status: ResponseStatus, open val data: Map<String, Any>)
