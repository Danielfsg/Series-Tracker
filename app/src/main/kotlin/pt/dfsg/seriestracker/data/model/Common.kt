package pt.dfsg.seriestracker.data.model

import android.annotation.SuppressLint
import android.arch.persistence.room.*
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import pt.dfsg.seriestracker.data.room.RoomContract
import pt.dfsg.seriestracker.data.room.RoomConverters

data class APIError(
    var name: String?,
    var message: String?,
    var code: Int?,
    var status: Int?,
    var previous: Previous?
) {
    data class Previous(
        var name: String?,
        var message: String?,
        var code: Int?
    )
}

@SuppressLint("ParcelCreator")
@Parcelize
data class Image(
    var medium: String?,
    var original: String?
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Schedule(
    var time: String?,
    var days: List<String?>?
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Rating(
    var average: Double?
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Externals(
    var tvrage: Int?,
    var thetvdb: Int?,
    var imdb: String?
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Network(
    @ColumnInfo(name = "id_network") var id: Int?,
    @ColumnInfo(name = "network_name") var name: String?,
    @SerializedName("country") @Embedded var countryNetwork: CountryNetwork?
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class WebChannel(
    @ColumnInfo(name = "id_webchannel") var id: Int?,
    @ColumnInfo(name = "webchannel_name") var name: String?,
    @SerializedName("country") @Embedded var countryWeb: CountryWebChannel?
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CountryNetwork(
    @ColumnInfo(name = "net_country_name") var name: String?,
    @ColumnInfo(name = "net_country_code") var code: String?,
    @ColumnInfo(name = "net_country_timezone") var timezone: String?
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CountryWebChannel(
    @ColumnInfo(name = "web_country_name") var name: String?,
    @ColumnInfo(name = "web_country_code") var code: String?,
    @ColumnInfo(name = "web_country_timezone") var timezone: String?
) : Parcelable