package pt.dfsg.seriestracker.data.room

class RoomContract {
    companion object {
        const val DATABASE_NAME = "db-series"
        const val DATABASE_VERSION = 6
        const val TABLE_SHOW = "show"
        const val TABLE_SEASON = "season"
        const val TABLE_EPISODE = "episode"
    }
}