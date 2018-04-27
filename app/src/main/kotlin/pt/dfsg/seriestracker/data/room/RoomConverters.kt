package pt.dfsg.seriestracker.data.room

import android.arch.persistence.room.TypeConverter

class RoomConverters {

    @TypeConverter
    fun storedStringToList(value: String): List<String> {
        return value.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }
    }

    @TypeConverter
    fun listToStoredString(list: List<String>): String {
        var value = ""
        for (string in list) value += "$string,"
        return value
    }
}
