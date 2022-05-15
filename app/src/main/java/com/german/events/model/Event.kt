package com.german.events.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Event(
    var createdBy: String? = null,
    @DocumentId var eventId: String? = null,
    var name: String? = null,
    var address: String? = null,
    var timestamp: Timestamp? = null
)