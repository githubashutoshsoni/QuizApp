package com.style.quiztrivia.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class UserModel @JvmOverloads constructor(

    @PrimaryKey
    @ColumnInfo
    var userName: String = "",

    @ColumnInfo
    var mobileNumber: String = "",

    @ColumnInfo
    var email: String = ""
)