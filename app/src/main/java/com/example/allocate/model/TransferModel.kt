package com.example.allocate.model

data class TransferModel(
    var transferId: Int,
    var toHospital: String,
    var fromHospital: String,
    var patientAge: Int,
    var patientName: String,
    var address: String
)