package com.example.bitcoinstore.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    // Taxa fixa para simulação: 1 BTC = R$ 600.000,00
    const val BRL_PER_BTC = 600_000.0
    private val nfBRL = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    fun formatBRL(value: Double): String = nfBRL.format(value)

    /** Formata BTC com 8 casas e símbolo ₿ */
    fun formatBTC(btc: BigDecimal): String {
        val fixed = btc.setScale(8, RoundingMode.DOWN)
        return "₿ $fixed"
    }

    fun brlToBtc(brl: Double): BigDecimal {
        val btc = brl / BRL_PER_BTC
        return BigDecimal.valueOf(btc).setScale(8, RoundingMode.HALF_UP)
    }

    fun btcToBRL(btc: BigDecimal): Double {
        return btc.multiply(BigDecimal.valueOf(BRL_PER_BTC)).toDouble()
    }

    fun btcToSats(btc: BigDecimal): Long {
        return btc.multiply(BigDecimal("100000000")).setScale(0, RoundingMode.HALF_UP).toLong()
    }

    fun satsToBtc(sats: Long): BigDecimal {
        return BigDecimal(sats).divide(BigDecimal("100000000"), 8, RoundingMode.DOWN)
    }
}
