package williamlopes.project.rtcontrol.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

fun getDateHourWithMinusSignal(date: Date?): String? {
    return getDateHour(date)?.replace("/", "-")
}

fun getDateHour(date: Date?): String? {
    if (date == null) {
        return "-"
    }
    val locale = Locale.getDefault()
    val sdfDefaultLocal: SimpleDateFormat
    sdfDefaultLocal = SimpleDateFormat("dd/MM/yyyy HH:mm", locale)
    val str = sdfDefaultLocal.format(date)
    return str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1)
}

