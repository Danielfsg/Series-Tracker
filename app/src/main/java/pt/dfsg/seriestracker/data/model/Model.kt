package pt.dfsg.seriestracker.data.model

import com.google.gson.annotations.SerializedName

object Model {

    data class Search(
        @SerializedName("score") var score: Double?,
        @SerializedName("show") var show: Show?
    )

    data class Show(
        @SerializedName("id") var id: Int?,
        @SerializedName("url") var url: String?,
        @SerializedName("name") var name: String?,
        @SerializedName("type") var type: String?,
        @SerializedName("language") var language: String?,
        @SerializedName("genres") var genres: List<String?>?,
        @SerializedName("status") var status: String?,
        @SerializedName("runtime") var runtime: Int?,
        @SerializedName("premiered") var premiered: String?,
        @SerializedName("officialSite") var officialSite: String?,
        @SerializedName("schedule") var schedule: Schedule?,
        @SerializedName("rating") var rating: Rating?,
        @SerializedName("weight") var weight: Int?,
        @SerializedName("network") var network: Network?,
        @SerializedName("webChannel") var webChannel: WebChannel?,
        @SerializedName("externals") var externals: Externals?,
        @SerializedName("image") var image: Image?,
        @SerializedName("summary") var summary: String?,
        @SerializedName("updated") var updated: Int?,
        @SerializedName("_links") var links: Links?
    )

    data class Schedule(
        @SerializedName("time") var time: String?,
        @SerializedName("days") var days: List<String?>?
    )

    data class Image(
        @SerializedName("medium") var medium: String?,
        @SerializedName("original") var original: String?
    )

    data class Links(
        @SerializedName("self") var self: Self?,
        @SerializedName("previousepisode") var previousepisode: Previousepisode?
    )

    data class Self(
        @SerializedName("href") var href: String?
    )

    data class Previousepisode(
        @SerializedName("href") var href: String?
    )

    data class Network(
        @SerializedName("id") var id: Int?,
        @SerializedName("name") var name: String?,
        @SerializedName("country") var country: Country?
    )

    data class WebChannel(
        @SerializedName("id") var id: Int?,
        @SerializedName("name") var name: String?,
        @SerializedName("country") var country: Country?
    )

    data class Country(
        @SerializedName("name") var name: String?,
        @SerializedName("code") var code: String?,
        @SerializedName("timezone") var timezone: String?
    )

    data class Rating(
        @SerializedName("average") var average: Double?
    )

    data class Externals(
        @SerializedName("tvrage") var tvrage: Int?,
        @SerializedName("thetvdb") var thetvdb: Int?,
        @SerializedName("imdb") var imdb: String?
    )
}
