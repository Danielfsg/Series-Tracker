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
@Entity(tableName = RoomContract.TABLE_SEASON)
data class Season(
    var id_show: Long,
    @PrimaryKey
    var id: Long,
    var url: String?,
    var number: Int?,
    var name: String?,
    var episodeOrder: Int?,
    var premiereDate: String?,
    var endDate: String?,
    var summary: String?,
    @Embedded var image: Image?,
    @Embedded var network: Network?,
    @Embedded var webChannel: WebChannel?
) : Parcelable
