package pt.dfsg.seriestracker.data.model

import android.annotation.SuppressLint
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pt.dfsg.seriestracker.data.room.RoomContract
import pt.dfsg.seriestracker.data.room.RoomConverters

@SuppressLint("ParcelCreator")
@Parcelize
@Entity(tableName = RoomContract.TABLE_SHOW)
@TypeConverters(RoomConverters::class)
data class Show(
    @PrimaryKey
    var id: Long,
    var url: String?,
    var name: String?,
    var type: String?,
    var summary: String?,
    var language: String?,
    var genres: List<String?>?,
    var status: String?,
    var runtime: Int?,
    var premiered: String?,
    var officialSite: String?,
    var weight: Int?,
    var updated: Int?,
    @Embedded var image: Image?,
    @Embedded var schedule: Schedule?,
    @Embedded var rating: Rating?,
    @Embedded var externals: Externals?,
    @Embedded var network: Network?,
    @Embedded var webChannel: WebChannel?,
    var isFavorite: Boolean = false
) : Parcelable