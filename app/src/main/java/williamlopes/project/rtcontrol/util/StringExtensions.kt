package williamlopes.project.rtcontrol.util

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.Normalizer
import java.text.NumberFormat
import java.text.ParseException
import java.util.*
import kotlin.collections.HashMap

fun String.brasilRealToDouble(): Double = NumberFormat.getInstance(Locale.getDefault()).parse(this.replace("R$", "")).toDouble()

fun String.removeSpecialCharacters() = Normalizer.normalize(this, Normalizer.Form.NFD).replace("[^\\p{ASCII}]".toRegex(), "")

fun String.unmask(): String {
    return this.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "").replace("[/]".toRegex(), "")
        .replace("[(]".toRegex(), "").replace(
            "[ ]".toRegex(), ""
        ).replace("[:]".toRegex(), "").replace("[)]".toRegex(), "")
}

fun String.splitDate(): HashMap<String, Int> {
    val KEY_DAY = "day"
    val KEY_MONTH = "month"
    val KEY_YEAR = "year"

    return if (this.isNotEmpty()) {
        val hashMap = hashMapOf<String, Int>()
        val list = this.split("/")
        hashMap[KEY_DAY] = list.first().toInt()
        hashMap[KEY_MONTH] = list[1].toInt() - 1
        hashMap[KEY_YEAR] = list.last().toInt()
        hashMap
    } else {
        hashMapOf()
    }

}

fun String.Companion.empty() = ""

fun String.convertToDoublePercentage(): Double {
    val replaceable =
        String.format("[%s,.\\s]", "%")
    val cleanString = this.replace(replaceable.toRegex(), String.Companion.empty())
    var convertedNumber = 0.0
    val nf = DecimalFormat("#.##")
    try {
        convertedNumber = nf.parse(cleanString).toDouble()
        if (convertedNumber.toString().length == 3) {
            convertedNumber /= 100
        } else if (convertedNumber.toString().length == 4) {
            convertedNumber /= 100
        } else if (convertedNumber.toString().length == 5) {
            convertedNumber /= 100
        } else if (convertedNumber.toString().length == 6) {
            convertedNumber /= 100
        } else if (convertedNumber.toString().length >= 7) {
            convertedNumber = 0.0
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return convertedNumber
}

private val locale: Locale? = Locale.getDefault()

fun String.convertToDoubleValue(): Double? {
    val replaceable =
        String.format("[%s,.\\s]", "R$")
    val cleanString = this.replace(replaceable.toRegex(), String.Companion.empty())
    var parsed = parseToBigDecimal(cleanString)
    var convertedNumber = 0.0
    val nf = DecimalFormat("##")
    try {
        convertedNumber = nf.parse(NumberFormat.getCurrencyInstance(locale).format(parsed)).toDouble()
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return parsed?.toDouble()
}

fun parseToBigDecimal(value: String?): BigDecimal? {
    val replaceable =
        String.format("[%s,.\\s]", "R$")
    val cleanString = value?.replace(replaceable.toRegex(), String.Companion.empty())
    return try {
        BigDecimal(cleanString).setScale(
            2, BigDecimal.ROUND_FLOOR
        ).divide(BigDecimal(100), BigDecimal.ROUND_FLOOR)
    } catch (e: NumberFormatException) {
        BigDecimal(0)
    }
}


