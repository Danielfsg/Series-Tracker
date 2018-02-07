package pt.dfsg.seriestracker.data.local

import android.arch.persistence.room.TypeConverter

class ListStringConverter {

    @TypeConverter
    fun storedStringToList(value: String): List<String> {
        return value.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }
    }

    @TypeConverter
    fun listToStoredString(list: List<String>): String {
        var value = ""
        for (string in list) value += string + ","
        return value
    }
}
