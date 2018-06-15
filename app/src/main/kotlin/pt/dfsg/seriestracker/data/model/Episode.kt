package pt.dfsg.seriestracker.data.model

import android.annotation.SuppressLint
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pt.dfsg.seriestracker.data.room.RoomContract

@SuppressLint("ParcelCreator")
@Parcelize
@Entity(tableName = RoomContract.TABLE_EPISODE)
data class Episode(
    var id_show: Long,
    @PrimaryKey
    var id: Long,
    var url: String?,
    var name: String?,
    var season: Int?,
    var number: Int?,
    var airdate: String?,
    var airtime: String?,
    var airstamp: String?,
    var runtime: Int?,
    @Embedded var image: Image?,
    var summary: String?,
    var watched: Boolean = false
) : Parcelable
