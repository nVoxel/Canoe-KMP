package com.voxeldev.canoe.utils.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase

/**
 * @author nvoxel
 */
class AndroidAnalytics : CommonAnalytics {

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    override fun logEvent(event: CustomEvent) = firebaseAnalytics.logEvent(name = event.name, block = {})
}
