package com.example.gashistory.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gasolina (
    val data: String = "", //primary key
    val preco: String = ""
):Parcelable
