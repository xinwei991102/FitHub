package com.example.fithub

data class Profile(
    var name: String? = "",
    var gender: String? = "",
    var height: Double = 0.0,
    var weight: Double = 0.0,
    var downloadUrl: String = ""
) {
    constructor() : this("", "", 0.0, 0.0, "")
}