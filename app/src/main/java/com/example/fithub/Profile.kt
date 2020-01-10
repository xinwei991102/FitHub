package com.example.fithub

import java.net.URL

data class Profile(
    var gender: String = "",
    var height: Double = 0.0,
    var name: String = "",
    var weight: Double = 0.0,
    var downloadUrl: String = ""
){
    constructor() : this("", 0.0, "", 0.0, "")
}
