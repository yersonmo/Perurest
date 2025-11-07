package com.perurest.ui.format

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun Double.toChileanPeso(): String {
    val localeCl = Locale("es", "CL")
    val nf = NumberFormat.getCurrencyInstance(localeCl)
    nf.currency = Currency.getInstance("CLP")
    nf.maximumFractionDigits = 0
    nf.minimumFractionDigits = 0
    return nf.format(this)
}