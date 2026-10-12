package com.example.slotio.navigation

object Routes {
    const val Login = "login"
    const val Register = "register"
    const val Home = "home"
    const val Booking = "booking"
    const val Profile = "profile"
    const val Search = "search"
    const val VenueDetail = "venue/{venueId}"
    fun venueDetail(id: Int) = "venue/$id"
}