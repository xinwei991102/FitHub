package com.example.fithub

import android.graphics.Color

data class ExerciseRecord(
    var uid: String = "",
    var evColor: Int,
    var millisecond: Long = 0L,
    var data : String = ""
) {
    constructor() : this("", 0,0L, "")
}