package com.german.events.model

import com.google.firebase.firestore.DocumentId

data class Subscription (
    @DocumentId var subscriptionId: String? = null,
    var idSubscriptor: String? = null,
    var idEvent: String? = null
)