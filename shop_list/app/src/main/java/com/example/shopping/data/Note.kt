package com.example.shopping.data

import kotlinx.serialization.*

@Serializable
data class Note(var Name:    String,
                var Value:   Int,
                var Metrics: String)

