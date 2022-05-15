package com.german.events.model

data class Event(
    var createdBy: String? = null,
    var eventId: String? = null,
    var name: String? = null,
    var address: String? = null,
    var date: String? = null,
    var time: String? = null
)